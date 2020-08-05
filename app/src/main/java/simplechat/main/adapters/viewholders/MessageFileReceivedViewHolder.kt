package simplechat.main.adapters.viewholders

import simplechat.main.adapters.OnMessageClickListener
import simplechat.main.adapters.base.BaseViewHolder
import simplechat.main.databinding.MessageFileReceivedItemBinding
import simplechat.main.models.Message

class MessageFileReceivedViewHolder(private val dataBinding: MessageFileReceivedItemBinding) :
    BaseViewHolder<Message, OnMessageClickListener>(dataBinding.root) {
    override fun bind(item: Message, onClickListener: OnMessageClickListener?) {
        dataBinding.message = item
    }
}