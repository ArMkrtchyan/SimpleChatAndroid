package simplechat.main.adapters.viewholders

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.bumptech.glide.Glide
import simplechat.main.adapters.OnItemClickListener
import simplechat.main.adapters.base.BaseViewHolder
import simplechat.main.databinding.MessagePhotoSendItemBinding
import simplechat.main.models.Message
import simplechat.main.utils.Utils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MessagePhotoSendViewHolder(private val dataBinding: MessagePhotoSendItemBinding) :
    BaseViewHolder<Message, OnItemClickListener<Message>>(dataBinding.root) {
    override fun bind(item: Message, onClickListener: OnItemClickListener<Message>?) {
        dataBinding.message = item
        Log.i("PhotosTag", "item: $item")
        val mBitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(dataBinding.root.context.contentResolver, Uri.parse(item.uri))
        } else {
            try {
                val source = ImageDecoder.createSource(dataBinding.root.context.contentResolver, Uri.parse(item.uri))
                ImageDecoder.decodeBitmap(source)
            } catch (e: Exception) {
                null
            }

        }
        if (mBitmap == null) {

            return
        }
        val bitmap = Utils.resizeImage(dataBinding.root.context, mBitmap, Uri.parse(item.uri))
        val file = File(dataBinding.root.context.cacheDir, bitmap.hashCode().toString())
        file.createNewFile()
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
        val bitmapData = bos.toByteArray()
        val fos = FileOutputStream(file)
        fos.write(bitmapData)
        fos.flush()
        fos.close()
        Glide.with(dataBinding.root.context).load(bitmap).into(dataBinding.image)
    }
}