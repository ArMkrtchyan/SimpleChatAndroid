package simplechat.main.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import simplechat.main.database.dao.MessageDao
import simplechat.main.database.entity.MessageEntity
import simplechat.main.database.mappers.MessagesMapper
import simplechat.main.models.Message

class MessageRepository(private val messageDao: MessageDao) {
    suspend fun insertMessage(message: MessageEntity): MutableLiveData<Message> {
        val messagesLiveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch(CoroutineExceptionHandler { _, exception ->
            Log.e("DBEXCEPTION",
                "insertMessage in repository: Caught $exception with suppressed ${exception.suppressed.contentToString()}")

        }) {
            withContext(Dispatchers.IO) { messageDao.insert(message) }
            messagesLiveData.postValue(MessagesMapper.messageEntityToMessage(messageDao.getLastMessage(message.chatId)))
        }
        return messagesLiveData

    }

    suspend fun insertMessages(messages: List<MessageEntity>, chatId: Int): MutableLiveData<ArrayList<Message>> {
        val messagesLiveData = MutableLiveData(ArrayList<Message>())
        CoroutineScope(Dispatchers.IO).launch(CoroutineExceptionHandler { _, exception ->
            Log.e("DBEXCEPTION",
                "insertMessages in repository: Caught $exception with suppressed ${exception.suppressed.contentToString()}")

        }) {
            withContext(Dispatchers.IO) { messageDao.insertMessages(messages) }
            messagesLiveData.postValue(MessagesMapper.messageEntitiesToMessages(messageDao.getAllMessages(chatId)))
        }
        return messagesLiveData

    }

    suspend fun findAllMessages(chatId: Int): MutableLiveData<ArrayList<Message>> {
        val messagesLiveData = MutableLiveData(ArrayList<Message>())
        CoroutineScope(Dispatchers.IO).launch(CoroutineExceptionHandler { _, exception ->
            Log.e("DBEXCEPTION",
                "findAllMessages in repository: Caught $exception with suppressed ${exception.suppressed.contentToString()}")

        }) {
            messagesLiveData.postValue(MessagesMapper.messageEntitiesToMessages(messageDao.getAllMessages(chatId)))
        }
        return messagesLiveData
    }
}