package simplechat.main.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import simplechat.main.SimpleChatApplication
import simplechat.main.database.entity.MessageEntity
import simplechat.main.database.mappers.MessagesMapper
import simplechat.main.models.Chat
import simplechat.main.models.Message

@ExperimentalCoroutinesApi
class MessageRepository {

    private val messageDao = SimpleChatApplication.getInstance().getChatDb().messageDao()

    suspend fun insertMessage(message: Message, mFlow: suspend (Flow<Message>) -> Unit) {
        mFlow(flow {
            emit(MessagesMapper.messageEntityToMessage(messageDao.getLastMessage(message.chatId)))
        }.flowOn(Dispatchers.IO).onStart {
            messageDao.insert(MessagesMapper.messageToMessageEntity(message))
        })
    }

    suspend fun insertMessages(messages: List<MessageEntity>, chatId: Int, mFlow: suspend (Flow<ArrayList<Message>>) -> Unit) {
        mFlow(flow {
            emit(MessagesMapper.messageEntitiesToMessages(messageDao.getAllMessages(chatId)))
        }.flowOn(Dispatchers.IO).onStart {
            messageDao.insertMessages(messages)
        })
    }

    suspend fun findAllMessages(chatId: Int, mFlow: suspend (Flow<ArrayList<Message>>) -> Unit) {
        mFlow(flow {
            emit(MessagesMapper.messageEntitiesToMessages(messageDao.getAllMessages(chatId)))
        }.flowOn(Dispatchers.IO))
    }
}