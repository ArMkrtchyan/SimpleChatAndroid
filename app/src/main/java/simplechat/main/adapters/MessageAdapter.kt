package simplechat.main.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import simplechat.main.adapters.base.BaseAdapter
import simplechat.main.adapters.base.BaseViewHolder
import simplechat.main.adapters.diffutils.MessageDiffUtilCallback
import simplechat.main.adapters.viewholders.*
import simplechat.main.databinding.*
import simplechat.main.models.Message
import simplechat.main.utils.Utils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MessageAdapter : BaseAdapter<BaseViewHolder<Message, OnMessageClickListener>, Message, OnMessageClickListener>() {

    private val typeMessageSend = 1
    private val typeMessageReceived = 2
    private val typeMessagePhotoSend = 3
    private val typeMessagePhotoReceived = 4
    private val typeMessageFileSend = 5
    private val typeMessageFileReceived = 6

    private val data = ArrayList<Message>()
    private var onMessageClickListener: OnMessageClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Message, OnMessageClickListener> {
        return when (viewType) {
            typeMessageSend -> {
                MessageTextSendViewHolder(MessageTextSendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            typeMessageReceived -> {
                MessageTextReceivedViewHolder(
                    MessageTextReceivedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            typeMessagePhotoSend -> {
                MessagePhotoSendViewHolder(MessagePhotoSendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            typeMessagePhotoReceived -> {
                MessagePhotoReceivedViewHolder(
                    MessagePhotoReceivedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            typeMessageFileSend -> {
                MessageFileSendViewHolder(MessageFileSendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            typeMessageFileReceived -> {
                MessageFileReceivedViewHolder(
                    MessageFileReceivedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> {
                MessageTextSendViewHolder(MessageTextSendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int): Int {
        return data[position].messageType
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Message, OnMessageClickListener>, position: Int) {
        holder.bind(data[position], onMessageClickListener)
    }

    override fun setItems(items: List<Message>?) {
        items?.let {
            val oldData = ArrayList<Message>().apply { addAll(data) }
            data.clear()
            data.addAll(it)
            var date = "date"
            data.asReversed().map { message ->
                message.day = setDate(message.createdAt)
                message.time = setTime(message.createdAt)
                if (message.day != date) {
                    message.isDateMustShow = true
                    date = message.day
                } else {
                    message.isDateMustShow = false
                }
            }
            val callback = MessageDiffUtilCallback(data, oldData)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            Log.i("MessageItemsTag", data.toString())
        }
    }

    override fun setListener(listener: OnMessageClickListener?) {
        onMessageClickListener = listener
    }

    private fun setDate(updatedAt: String): String {
        val date = Utils.parseStringToDate(updatedAt)
        return SimpleDateFormat("EEE, dd MMM", Locale("en")).format(date)
    }

    private fun setTime(updatedAt: String): String {
        val date = Utils.parseStringToDate(updatedAt)
        return SimpleDateFormat("HH:mm", Locale("en")).format(date)
    }
}