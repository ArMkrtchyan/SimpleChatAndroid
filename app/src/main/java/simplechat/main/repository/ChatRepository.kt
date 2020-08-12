package simplechat.main.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import simplechat.main.SimpleChatApplication
import simplechat.main.database.entity.ChatEntity
import simplechat.main.database.mappers.ChatsMapper
import simplechat.main.models.Chat

@ExperimentalCoroutinesApi
class ChatRepository {

    private val chatDao = SimpleChatApplication.getInstance().getChatDb().chatDao()
    private val messageDao = SimpleChatApplication.getInstance().getChatDb().messageDao()

    fun insertChat(chat: ChatEntity): Flow<ArrayList<Chat>> {
        return flow {
            emit(ChatsMapper.chatEntitiesToChats(chatDao.getAllChats()))
        }.onStart {
            chatDao.insert(chat)
        }.flowOn(Dispatchers.IO)
    }

    fun insertChats(chats: List<ChatEntity>): Flow<ArrayList<Chat>> {
        return flow {
            emit(ChatsMapper.chatEntitiesToChats(chatDao.getAllChats()))
        }.onStart {
            chatDao.insertChats(chats)
        }.flowOn(Dispatchers.IO)

    }

    fun findAllChats(): Flow<ArrayList<Chat>> {
        return flow {
            emit(ChatsMapper.chatEntitiesToChats(chatDao.getAllChats()))
        }.flowOn(Dispatchers.IO)
    }

    fun updateChat(chat: ChatEntity): Flow<Boolean> {
        return flow {
            emit(true)
        }.onStart {
            chatDao.updateChat(chat)
        }.flowOn(Dispatchers.IO)
    }

    fun deleteChat(chat: ChatEntity): Flow<ArrayList<Chat>> {
        return flow {
            emit(ChatsMapper.chatEntitiesToChats(chatDao.getAllChats()))
        }.onStart {
            chatDao.deleteChat(chat)
            messageDao.deleteMessagesWithChatId(chat.id)
        }.flowOn(Dispatchers.IO)

    }

}