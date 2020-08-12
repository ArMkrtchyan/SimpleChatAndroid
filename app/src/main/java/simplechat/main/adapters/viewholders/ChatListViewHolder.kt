package simplechat.main.adapters.viewholders

import simplechat.main.adapters.OnItemClickListener
import simplechat.main.adapters.base.BaseViewHolder
import simplechat.main.databinding.ChatListItemBinding
import simplechat.main.models.Chat

class ChatListViewHolder(private val dataBinding: ChatListItemBinding) :
    BaseViewHolder<Chat, OnItemClickListener<Chat>>(dataBinding.root) {
    override fun bind(item: Chat, onClickListener: OnItemClickListener<Chat>?) {
        dataBinding.chat = item
        dataBinding.root.setOnClickListener {
            onClickListener?.onItemClick(dataBinding.root, adapterPosition, item)
        }
        dataBinding.root.setOnLongClickListener {
            onClickListener?.onItemLongClick(dataBinding.root, adapterPosition, item)
            false
        }
    }
}