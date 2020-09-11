package simplechat.main.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import simplechat.main.adapters.MessageAdapter
import simplechat.main.database.mappers.ChatsMapper
import simplechat.main.models.Chat
import simplechat.main.models.Message
import simplechat.main.repository.ChatRepository
import simplechat.main.repository.MessageRepository
import simplechat.main.viewmodels.base.BaseViewModel

@ExperimentalCoroutinesApi
class MessagesViewModel : BaseViewModel() {
    private lateinit var context: Context
    private val messageRepository by lazy { MessageRepository() }
    private val chatRepository by lazy { ChatRepository() }
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

    private fun setMessageListLiveData(data: ArrayList<Message>) {
        messageListLiveData.value = data
    }

    fun setContext(context: Context) {
        this.context = context
    }

    fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
    }


    fun getMessages(chat: Chat) {
        viewModelScope.launch {
            Log.i("MessagesListTag", "getMessages-> chatId: ${chat.id}")
            messageRepository.findAllMessages(chat.id) { flow ->
                flow.collect {
                    Log.i("MessagesListTag", "getMessages: $it")
                    messageList.clear()
                    messageList.addAll(it)
                    setMessageListLiveData(messageList)
                }
            }
        }
    }


   suspend fun addMessage(message: Message, chat: Chat, mFlow: suspend (Flow<Boolean>) -> Unit) {
        mFlow(flow {
            messageRepository.insertMessage(message) { flow ->
                flow.collect {
                    Log.i("MessagesListTag", "insertMessage: $it")
                    messageList.add(0, it)
                    setMessageListLiveData(messageList)
                    emit(true)
                }
                chatRepository.updateChat(ChatsMapper.chatToChatEntity(chat)) { flow ->
                    flow.collect { }
                }
            }
        })
    }
}