package simplechat.main.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import simplechat.main.adapters.ChatListAdapter
import simplechat.main.database.entity.ChatEntity
import simplechat.main.database.mappers.ChatsMapper
import simplechat.main.models.Chat
import simplechat.main.repository.ChatRepository

@ExperimentalCoroutinesApi
class ChatListViewModel : ViewModel() {

    private lateinit var context: Context
    private val chatsRepository by lazy { ChatRepository() }
    private val chatList = ArrayList<Chat>()
    private val chatListLiveData = MutableLiveData(chatList)
    private val chatListAdapter by lazy { ChatListAdapter() }
    private lateinit var lifecycleOwner: LifecycleOwner

    fun getAdapter(): ChatListAdapter {
        return chatListAdapter
    }

    fun getChatListLiveData(): MutableLiveData<ArrayList<Chat>> {
        return chatListLiveData
    }

    private fun setChatListLiveData(data: ArrayList<Chat>) {
        chatListLiveData.value = data
    }

    fun setContext(context: Context) {
        this.context = context
    }

    fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
    }

    fun getChats() {
        viewModelScope.launch {
            chatsRepository.findAllChats { flow ->
                flow.collect {
                    chatList.clear()
                    chatList.addAll(it)
                    Log.i("ChatListTag", chatList.toString())
                    setChatListLiveData(chatList)
                }

            }
        }
    }

    fun addChat(chatEntity: ChatEntity) {
        viewModelScope.launch {
            chatsRepository.insertChat(chatEntity) { flow ->
                flow.collect {
                    chatList.clear()
                    chatList.addAll(it)
                    Log.i("ChatListTag", chatList.toString())
                    setChatListLiveData(chatList)
                }
            }
        }
    }

    fun updateChat(chat: ChatEntity) {
        viewModelScope.launch {
            chatsRepository.updateChat(chat) { flow ->
                flow.collect {
                    for (i in 0 until chatList.size) {
                        if (chatList[i].id == ChatsMapper.chatEntityToChat(chat).id) {
                            chatList[i] = ChatsMapper.chatEntityToChat(chat)
                            break
                        }
                    }
                    chatList.run {
                        sortWith(Comparator { o1, o2 -> o2.lastMessageDate.compareTo(o1.lastMessageDate) })
                    }
                    Log.i("ChatListTag", chatList.toString())
                    setChatListLiveData(chatList)
                }
            }
        }
    }

    fun deleteChat(chat: ChatEntity) {
        viewModelScope.launch {
            chatsRepository.deleteChat(chat) { flow ->
                flow.collect {
                    chatList.clear()
                    chatList.addAll(it)
                    Log.i("ChatListTag", chatList.toString())
                    setChatListLiveData(chatList)
                }
            }
        }
    }
}