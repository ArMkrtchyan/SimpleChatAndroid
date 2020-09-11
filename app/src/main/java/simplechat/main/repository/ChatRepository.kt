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

    suspend fun insertChat(chat: ChatEntity, mFlow: suspend (Flow<ArrayList<Chat>>) -> Unit) {
        mFlow(flow {
            emit(ChatsMapper.chatEntitiesToChats(chatDao.getAllChats()))
        }.onStart {
            chatDao.insert(chat)
        }.flowOn(Dispatchers.IO))
    }

    suspend fun insertChats(chats: List<ChatEntity>, mFlow: suspend (Flow<ArrayList<Chat>>) -> Unit) {
        mFlow(flow {
            emit(ChatsMapper.chatEntitiesToChats(chatDao.getAllChats()))
        }.onStart {
            chatDao.insertChats(chats)
        }.flowOn(Dispatchers.IO))

    }

    suspend fun findAllChats(mFlow: suspend (Flow<ArrayList<Chat>>) -> Unit) {
        mFlow(flow {
            emit(ChatsMapper.chatEntitiesToChats(chatDao.getAllChats()))
        }.flowOn(Dispatchers.IO))
    }

    suspend fun updateChat(chat: ChatEntity, mFlow: suspend (Flow<Boolean>) -> Unit) {
        mFlow(flow {
            emit(true)
        }.onStart {
            chatDao.updateChat(chat)
        }.flowOn(Dispatchers.IO))
    }

    suspend fun deleteChat(chat: ChatEntity, mFlow: suspend (Flow<ArrayList<Chat>>) -> Unit) {
        mFlow(flow {
            emit(ChatsMapper.chatEntitiesToChats(chatDao.getAllChats()))
        }.onStart {
            chatDao.deleteChat(chat)
            messageDao.deleteMessagesWithChatId(chat.id)
        }.flowOn(Dispatchers.IO))

    }

}