package simplechat.main.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    private val EOF = -1
    private val DEFAULT_BUFFER_SIZE = 1024 * 4

    @JvmStatic
    fun parseStringToDate(dateStr: String?): Date {
        val tz = TimeZone.getTimeZone("GMT")
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale("en")).apply {
            timeZone = tz
        }
        return df.parse(dateStr)
    }

    @JvmStatic
    fun dateToStringWithTimeZone(date: Date): String? {
        val tz = TimeZone.getTimeZone("GMT")
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
            timeZone = tz
        }.format(date)
    }

    fun showKeyboard(activity: Activity, view: View) {
        view.requestFocus()
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    @JvmStatic
    fun getResizedBitmap(bm: Bitmap, newWidth: Float, newHeight: Float, isNecessaryToKeepOrig: Boolean): Bitmap {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth / width
        val scaleHeight = newHeight / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)
        if (!isNecessaryToKeepOrig) {
            bm.recycle()
        }
        return resizedBitmap
    }

    @JvmStatic
    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    @JvmStatic
    fun resizeImage(context: Context, bitmap: Bitmap, uri: Uri?): Bitmap {
        var bit = if (bitmap.width > 512) {
            val ratio = (bitmap.height.toDouble() / bitmap.width)
            val height = 512 * ratio
            getResizedBitmap(bitmap, 512f, height.toFloat(), true)
        } else {
            bitmap
        }
        //        val ei = ExifInterface(from(context, uri!!).absolutePath)
        //        val orientation =
        //            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        //        when (orientation) {
        //            ExifInterface.ORIENTATION_ROTATE_90 -> bit = rotateImage(bit, 90F)
        //            ExifInterface.ORIENTATION_ROTATE_180 -> bit = rotateImage(bit, 180F)
        //            ExifInterface.ORIENTATION_ROTATE_270 -> bit = rotateImage(bit, 270F)
        //        }
        return bit
    }

    @Throws(IOException::class)
    @JvmStatic
    fun from(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = getFileName(context, uri)
        val splitName = splitFileName(fileName)
        var tempFile = File.createTempFile(splitName[0], splitName[1])
        tempFile = rename(tempFile, fileName)
        tempFile.deleteOnExit()
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(tempFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        if (inputStream != null) {
            copy(inputStream, out!!)
            inputStream.close()
        }

        if (out != null) {
            out.close()
        }
        return tempFile
    }

    @JvmStatic
    private fun splitFileName(fileName: String): Array<String> {
        var name = fileName
        var extension = ""
        val i = fileName.lastIndexOf(".")
        if (i != -1) {
            name = fileName.substring(0, i)
            extension = fileName.substring(i)
        }

        return arrayOf(name, extension)
    }

    @JvmStatic
    private fun getFileName(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme.equals("content")) {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (cursor != null) {
                    cursor.close()
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf(File.separator)
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    @JvmStatic
    private fun rename(file: File, newName: String): File {
        val newFile = File(file.parent, newName)
        if (!newFile.equals(file)) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old $newName file")
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to $newName")
            }
        }
        return newFile
    }

    @JvmStatic
    @Throws(IOException::class)
    private fun copy(input: InputStream, output: FileOutputStream): Long {
        var count: Long = 0
        var n: Int
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        n = input.read(buffer)
        while (EOF != n) {
            output.write(buffer, 0, n)
            count += n.toLong()
            n = input.read(buffer)
        }
        return count
    }
}

