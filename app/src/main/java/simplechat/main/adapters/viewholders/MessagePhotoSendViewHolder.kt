package simplechat.main.adapters.viewholders

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import simplechat.main.adapters.OnItemClickListener
import simplechat.main.adapters.base.BaseViewHolder
import simplechat.main.databinding.MessagePhotoSendItemBinding
import simplechat.main.models.Message


class MessagePhotoSendViewHolder(private val dataBinding: MessagePhotoSendItemBinding) :
    BaseViewHolder<Message, OnItemClickListener<Message>>(dataBinding.root) {
    override fun bind(item: Message, onClickListener: OnItemClickListener<Message>?) {
        dataBinding.message = item
        Log.i("PhotosTag", "item: $item")
        Glide.with(dataBinding.root.context).load(Uri.parse(item.uri)).into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                val bitmap = (resource as BitmapDrawable).bitmap
                val ratio = (bitmap.height.toDouble() / bitmap.width)
                val height = 512 * ratio
                Glide.with(dataBinding.root.context).load(bitmap).apply(RequestOptions().override(512, height.toInt()))
                    .into(dataBinding.image)
            }
        })
        dataBinding.root.setOnClickListener {
            onClickListener?.onItemClick(dataBinding.image, adapterPosition, item)
        }
    }
}