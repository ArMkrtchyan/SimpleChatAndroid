package simplechat.main.repository

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import simplechat.main.database.dao.ChatDao
import simplechat.main.database.entity.ChatEntity
import simplechat.main.database.mappers.ChatsMapper
import simplechat.main.models.Chat

class ChatRepository(private val chatDao: ChatDao) {
    suspend fun insertChat(chat: ChatEntity): MutableLiveData<ArrayList<Chat>> {
        val chatsLiveData = MutableLiveData(ArrayList<Chat>())
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) { chatDao.insert(chat) }
            chatsLiveData.postValue(ChatsMapper.chatEntitiesToChats(chatDao.getAllChats()))
        }
        return chatsLiveData
    }

    suspend fun insertChats(chats: List<ChatEntity>): MutableLiveData<ArrayList<Chat>> {
        val chatsLiveData = MutableLiveData(ArrayList<Chat>())
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) { chatDao.insertChats(chats) }
            chatsLiveData.postValue(ChatsMapper.chatEntitiesToChats(chatDao.getAllChats()))
        }
        return chatsLiveData

    }

    suspend fun findAllChats(): MutableLiveData<ArrayList<Chat>> {
        val chatsLiveData = MutableLiveData(ArrayList<Chat>())
        CoroutineScope(Dispatchers.IO).launch {
            chatsLiveData.postValue(ChatsMapper.chatEntitiesToChats(chatDao.getAllChats()))
        }
        return chatsLiveData
    }

    fun updateChat(chat: ChatEntity): MutableLiveData<Boolean> {
        val chatsLiveData = MutableLiveData<Boolean>()
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) { chatDao.updateChat(chat) }
            chatsLiveData.postValue(true)
        }
        return chatsLiveData
    }

    fun deleteChat(chat: ChatEntity): MutableLiveData<ArrayList<Chat>> {
        val chatsLiveData = MutableLiveData(ArrayList<Chat>())
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) { chatDao.deleteChat(chat) }
            chatsLiveData.postValue(ChatsMapper.chatEntitiesToChats(chatDao.getAllChats()))
        }
        return chatsLiveData
    }

}