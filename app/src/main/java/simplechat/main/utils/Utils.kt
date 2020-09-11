package simplechat.main.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    fun <T> parseObjectToString(obj: T) = Gson().toJson(obj).toString()

    inline fun <reified T> parseStringToObject(str: String): T = Gson().fromJson<T>(str, T::class.java)

    fun parseStringToDate(dateStr: String?): Date {
        val tz = TimeZone.getTimeZone("GMT")
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale("en")).apply {
            timeZone = tz
        }
        return df.parse(dateStr)
    }

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

    fun resizeImage(bitmap: Bitmap): Bitmap {
        return if (bitmap.width > 512) {
            val ratio = (bitmap.height.toDouble() / bitmap.width)
            val height = 512f * ratio
            val scaleWidth = 512f / bitmap.width
            val scaleHeight = height / bitmap.height
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight.toFloat())
            Bitmap.createBitmap(bitmap, 0, 0, 512, height.toInt(), matrix, false)
        } else bitmap
    }

}

