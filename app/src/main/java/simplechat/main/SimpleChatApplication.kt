package simplechat.main

import android.app.Application
import simplechat.main.database.ChatDB

class SimpleChatApplication : Application() {

    companion object {
        private lateinit var instance: SimpleChatApplication
        fun getInstance() = instance
    }

    private var chatDb: ChatDB? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        chatDb = ChatDB.getDatabase(applicationContext)
    }

    fun getChatDb(): ChatDB {
        return if (chatDb == null) {
            chatDb = ChatDB.getDatabase(applicationContext)
            chatDb!!
        } else {
            chatDb!!
        }
    }
}