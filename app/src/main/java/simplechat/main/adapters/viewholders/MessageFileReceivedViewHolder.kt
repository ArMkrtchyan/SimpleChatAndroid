package simplechat.main.adapters.viewholders

import simplechat.main.adapters.OnItemClickListener
import simplechat.main.adapters.base.BaseViewHolder
import simplechat.main.databinding.MessageFileReceivedItemBinding
import simplechat.main.models.Message

class MessageFileReceivedViewHolder(private val dataBinding: MessageFileReceivedItemBinding) :
    BaseViewHolder<Message, OnItemClickListener<Message>>(dataBinding.root) {
    override fun bind(item: Message, onClickListener: OnItemClickListener<Message>?) {
        dataBinding.message = item
    }
}