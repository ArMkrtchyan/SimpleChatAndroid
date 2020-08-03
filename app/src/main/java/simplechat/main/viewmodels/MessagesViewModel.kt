package simplechat.main.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import simplechat.main.adapters.MessageAdapter
import simplechat.main.database.ChatDB
import simplechat.main.database.entity.MessageEntity
import simplechat.main.database.mappers.ChatsMapper
import simplechat.main.models.Chat
import simplechat.main.models.Message
import simplechat.main.repository.ChatRepository
import simplechat.main.repository.MessageRepository
import simplechat.main.viewmodels.base.BaseViewModel

class MessagesViewModel : BaseViewModel() {
    private lateinit var context: Context
    private val messageRepository by lazy { MessageRepository(ChatDB.getDatabase(context).messageDao()) }
    private val chatRepository by lazy { ChatRepository(ChatDB.getDatabase(context).chatDao()) }
    private val messageList = ArrayList<Message>()
    private val messageListLiveData = MutableLiveData(messageList)
    private val messageListAdapter by lazy { MessageAdapter() }
    private lateinit var lifecycleOwner: LifecycleOwner

    fun getAdapter(): MessageAdapter {
        return messageListAdapter
    }

    fun getMessageListLiveData(): MutableLiveData<ArrayList<Message>> {
        return messageListLiveData
    }

    fun setMessageListLiveData(data: ArrayList<Message>) {
        messageListLiveData.value = data
    }

    fun setContext(context: Context) {
        this.context = context
    }

    fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
    }


    fun getMessages(chat: Chat) {
        viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
            Log.e("DBEXCEPTION",
                "getMessage in viewModel: Caught $exception with suppressed ${exception.suppressed.contentToString()}")

        }) {
            Log.i("MessagesListTag", "getMessages-> chatId: ${chat.id}")
            messageRepository.findAllMessages(chat.id).observe(lifecycleOwner, Observer {
                Log.i("MessagesListTag", "getMessages: $it")
                messageList.clear()
                messageList.addAll(it)
                setMessageListLiveData(messageList)
            })
        }
    }

    fun addMessages() {
        viewModelScope.launch {

        }
    }

    fun addMessage(messageEntity: MessageEntity, chat: Chat): MutableLiveData<Boolean> {
        val success = MutableLiveData<Boolean>()
        viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
            Log.e("DBEXCEPTION",
                "AddMessage in viewModel: Caught $exception with suppressed ${exception.suppressed.contentToString()}")

        }) {
            messageRepository.insertMessage(messageEntity).observe(lifecycleOwner, Observer { message ->
                message?.let {
                    Log.i("MessagesListTag", "insertMessage: $it")
                    messageList.add(0, it)
                    setMessageListLiveData(messageList)
                    chatRepository.updateChat(ChatsMapper.chatToChatEntity(chat))
                    success.value = true
                }
            })
        }
        return success
    }
}