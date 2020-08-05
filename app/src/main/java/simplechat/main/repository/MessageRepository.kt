package simplechat.main.repository

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import simplechat.main.database.dao.MessageDao
import simplechat.main.database.entity.MessageEntity
import simplechat.main.database.mappers.MessagesMapper
import simplechat.main.models.Message

class MessageRepository(private val messageDao: MessageDao) {
    suspend fun insertMessage(message: MessageEntity): MutableLiveData<Message> {
        val messagesLiveData = MutableLiveData<Message>()
        withContext(Dispatchers.IO) { messageDao.insert(message) }
        messagesLiveData.postValue(
            MessagesMapper.messageEntityToMessage(withContext(Dispatchers.IO) { messageDao.getLastMessage(message.chatId) }))
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