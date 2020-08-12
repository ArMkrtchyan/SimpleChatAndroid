package simplechat.main.adapters.viewholders

import simplechat.main.adapters.OnItemClickListener
import simplechat.main.adapters.base.BaseViewHolder
import simplechat.main.databinding.MessageFileSendItemBinding
import simplechat.main.models.Message

class MessageFileSendViewHolder(private val dataBinding: MessageFileSendItemBinding) :
    BaseViewHolder<Message, OnItemClickListener<Message>>(dataBinding.root) {
    override fun bind(item: Message, onClickListener: OnItemClickListener<Message>?) {
        dataBinding.message = item
    }
}