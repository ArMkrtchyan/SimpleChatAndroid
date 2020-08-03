package simplechat.main.adapters

import simplechat.main.models.Chat

interface OnChatClickListener {
    fun onChatClick(chat: Chat)
    fun onChatLongClick(chat: Chat)
    fun deleteChat()
    fun editChat()
}
