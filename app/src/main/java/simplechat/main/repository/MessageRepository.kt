package simplechat.main.repository

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import simplechat.main.SimpleChatApplication
import simplechat.main.database.entity.MessageEntity
import simplechat.main.database.mappers.MessagesMapper
import simplechat.main.models.Message

class MessageRepository {

    private val messageDao = SimpleChatApplication.getInstance().getChatDb().messageDao()

    suspend fun insertMessage(message: Message): MutableLiveData<Message> {
        val messagesLiveData = MutableLiveData<Message>()
        withContext(Dispatchers.IO) { messageDao.insert(MessagesMapper.messageToMessageEntity(message)) }
        val mMessage = withContext(Dispatchers.IO) { messageDao.getLastMessage(message.chatId) }
        messagesLiveData.postValue(MessagesMapper.messageEntityToMessage(mMessage))
        return messagesLiveData

    }

    suspend fun insertMessages(messages: List<MessageEntity>, chatId: Int): MutableLiveData<ArrayList<Message>> {
        val messagesLiveData = MutableLiveData(ArrayList<Message>())
        withContext(Dispatchers.IO) { messageDao.insertMessages(messages) }
        messagesLiveData.postValue(
            MessagesMapper.messageEntitiesToMessages(withContext(Dispatchers.IO) { messageDao.getAllMessages(chatId) }))
        return messagesLiveData

    }

    suspend fun findAllMessages(chatId: Int): MutableLiveData<ArrayList<Message>> {
        val messagesLiveData = MutableLiveData(ArrayList<Message>())
        messagesLiveData.postValue(
            MessagesMapper.messageEntitiesToMessages(withContext(Dispatchers.IO) { messageDao.getAllMessages(chatId) }))
        return messagesLiveData
    }
}