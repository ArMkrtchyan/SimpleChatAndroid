package simplechat.main.adapters.viewholders

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import simplechat.main.adapters.OnItemClickListener
import simplechat.main.adapters.base.BaseViewHolder
import simplechat.main.databinding.MessagePhotoReceivedItemBinding
import simplechat.main.models.Message
import simplechat.main.utils.Utils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MessagePhotoReceivedViewHolder(private val dataBinding: MessagePhotoReceivedItemBinding) :
    BaseViewHolder<Message, OnItemClickListener<Message>>(dataBinding.root) {
    override fun bind(item: Message, onClickListener: OnItemClickListener<Message>?) {
        dataBinding.message = item
        Glide.with(dataBinding.root.context).load(Uri.parse(item.uri)).into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                val bitmap = (resource as BitmapDrawable).bitmap
                val ratio = (bitmap.height.toDouble() / bitmap.width)
                val height = 512f * ratio
                Log.i("MPhototag", "width: ${bitmap.width} height: ${bitmap.height} ratio: $ratio")
                Glide.with(dataBinding.root.context).load(bitmap).apply(RequestOptions().override(512, height.toInt()))
                    .into(dataBinding.image)
            }
        })
        dataBinding.root.setOnClickListener {
            onClickListener?.onItemClick(dataBinding.image, adapterPosition, item)
        }
    }
}