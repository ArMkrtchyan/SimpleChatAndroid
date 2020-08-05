package simplechat.main.adapters.viewholders

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.bumptech.glide.Glide
import simplechat.main.adapters.OnMessageClickListener
import simplechat.main.adapters.base.BaseViewHolder
import simplechat.main.databinding.MessagePhotoReceivedItemBinding
import simplechat.main.models.Message
import simplechat.main.utils.Utils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MessagePhotoReceivedViewHolder(private val dataBinding: MessagePhotoReceivedItemBinding) :
    BaseViewHolder<Message, OnMessageClickListener>(dataBinding.root) {
    override fun bind(item: Message, onClickListener: OnMessageClickListener?) {
        dataBinding.message = item
        val mBitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(dataBinding.root.context.contentResolver, Uri.parse(item.uri))
        } else {
            val source = ImageDecoder.createSource(dataBinding.root.context.contentResolver, Uri.parse(item.uri))
            ImageDecoder.decodeBitmap(source)

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