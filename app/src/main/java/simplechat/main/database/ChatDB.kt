package simplechat.main.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import simplechat.main.database.dao.ChatDao
import simplechat.main.database.dao.MessageDao
import simplechat.main.database.entity.ChatEntity
import simplechat.main.database.entity.MessageEntity

@Database(entities = [MessageEntity::class, ChatEntity::class], version = 1, exportSchema = true)
abstract class ChatDB : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun chatDao(): ChatDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ChatDB? = null

        fun getDatabase(context: Context): ChatDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, ChatDB::class.java, "chat_database").build()
                INSTANCE = instance
                return instance
            }
        }
    }

}