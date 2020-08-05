package simplechat.main.repository

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import simplechat.main.SimpleChatApplication
import simplechat.main.database.entity.ChatEntity
import simplechat.main.database.mappers.ChatsMapper
import simplechat.main.models.Chat

class ChatRepository {

    private val chatDao = SimpleChatApplication.getInstance().getChatDb().chatDao()

    suspend fun insertChat(chat: ChatEntity): MutableLiveData<ArrayList<Chat>> {
        val chatsLiveData = MutableLiveData(ArrayList<Chat>())
        withContext(Dispatchers.IO) { chatDao.insert(chat) }
        chatsLiveData.postValue(ChatsMapper.chatEntitiesToChats(withContext(Dispatchers.IO) { chatDao.getAllChats() }))
        return chatsLiveData
    }

    suspend fun insertChats(chats: List<ChatEntity>): MutableLiveData<ArrayList<Chat>> {
        val chatsLiveData = MutableLiveData(ArrayList<Chat>())
        withContext(Dispatchers.IO) { chatDao.insertChats(chats) }
        chatsLiveData.postValue(ChatsMapper.chatEntitiesToChats(withContext(Dispatchers.IO) { chatDao.getAllChats() }))
        return chatsLiveData

    }

    suspend fun findAllChats(): MutableLiveData<ArrayList<Chat>> {
        val chatsLiveData = MutableLiveData(ArrayList<Chat>())
        chatsLiveData.postValue(ChatsMapper.chatEntitiesToChats(withContext(Dispatchers.IO) { chatDao.getAllChats() }))
        return chatsLiveData
    }

    suspend fun updateChat(chat: ChatEntity): MutableLiveData<Boolean> {
        val chatsLiveData = MutableLiveData<Boolean>()
        withContext(Dispatchers.IO) { chatDao.updateChat(chat) }
        chatsLiveData.postValue(true)
        return chatsLiveData
    }

    suspend fun deleteChat(chat: ChatEntity): MutableLiveData<ArrayList<Chat>> {
        val chatsLiveData = MutableLiveData(ArrayList<Chat>())
        withContext(Dispatchers.IO) { chatDao.deleteChat(chat) }
        chatsLiveData.postValue(ChatsMapper.chatEntitiesToChats(withContext(Dispatchers.IO) { chatDao.getAllChats() }))
        return chatsLiveData
    }

}