package simplechat.main.adapters.viewholders

import simplechat.main.adapters.OnChatClickListener
import simplechat.main.adapters.base.BaseViewHolder
import simplechat.main.databinding.ChatListItemBinding
import simplechat.main.models.Chat

class ChatListViewHolder(val dataBinding: ChatListItemBinding) : BaseViewHolder<Chat, OnChatClickListener>(dataBinding.root) {
    override fun bind(item: Chat, onClickListener: OnChatClickListener?) {
        dataBinding.chat = item
        dataBinding.root.setOnClickListener {
            onClickListener?.onChatClick(item)
        }
        dataBinding.root.setOnLongClickListener {
            onClickListener?.onChatLongClick(item)
            false
        }
    }
}