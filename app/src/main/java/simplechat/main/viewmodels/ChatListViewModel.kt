package simplechat.main.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import simplechat.main.adapters.ChatListAdapter
import simplechat.main.database.ChatDB
import simplechat.main.database.entity.ChatEntity
import simplechat.main.database.mappers.ChatsMapper
import simplechat.main.models.Chat
import simplechat.main.repository.ChatRepository

class ChatListViewModel : ViewModel() {

    private lateinit var context: Context
    private val chatsRepository by lazy { ChatRepository(ChatDB.getDatabase(context).chatDao()) }
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

    fun setChatListLiveData(data: ArrayList<Chat>) {
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
            chatsRepository.findAllChats().observe(lifecycleOwner, Observer {
                chatList.clear()
                chatList.addAll(it)
                Log.i("ChatListTag", chatList.toString())
                setChatListLiveData(chatList)
            })
        }
    }

    fun addChats() {
        viewModelScope.launch {
            chatsRepository.insertChats(ChatsMapper.chatsToChatEntities(ArrayList<Chat>().apply {
                for (i in 0 until 30) {
                    add(Chat(i, "user number $i", "", false, ""))
                }
            })).observe(lifecycleOwner, Observer {
                setChatListLiveData(it)
            })
        }
    }

    fun addChat(chatEntity: ChatEntity) {
        viewModelScope.launch {
            chatsRepository.insertChat(chatEntity).observe(lifecycleOwner, Observer {
                chatList.clear()
                chatList.addAll(it)
                Log.i("ChatListTag", chatList.toString())
                setChatListLiveData(chatList)
            })
        }
    }

    fun updateChat(chat: ChatEntity) {
        viewModelScope.launch {
            chatsRepository.updateChat(chat).observe(lifecycleOwner, Observer { success ->
                success?.let {
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
            })
        }
    }

    fun deleteChat(chat: ChatEntity) {
        viewModelScope.launch {
            chatsRepository.deleteChat(chat).observe(lifecycleOwner, Observer {
                chatList.clear()
                chatList.addAll(it)
                Log.i("ChatListTag", chatList.toString())
                setChatListLiveData(chatList)
            })
        }
    }
}