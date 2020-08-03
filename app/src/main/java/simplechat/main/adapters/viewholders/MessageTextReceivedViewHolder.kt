package simplechat.main.adapters.viewholders

import simplechat.main.adapters.OnMessageClickListener
import simplechat.main.adapters.base.BaseViewHolder
import simplechat.main.databinding.MessageTextReceivedItemBinding
import simplechat.main.models.Message

class MessageTextReceivedViewHolder(val dataBinding: MessageTextReceivedItemBinding) :
    BaseViewHolder<Message, OnMessageClickListener>(dataBinding.root) {
    override fun bind(item: Message, onClickListener: OnMessageClickListener?) {
        dataBinding.message = item
    }
}