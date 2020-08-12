package simplechat.main.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import simplechat.main.database.entity.MessageEntity

@Dao
interface MessageDao {
    @Query(value = "SELECT * FROM messages WHERE chat_id = (:chatId) ORDER BY created_at DESC")
    suspend fun getAllMessages(chatId: Int): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(message: List<MessageEntity>)

    @Query(value = "DELETE FROM messages")
    suspend fun deleteAll()

    @Query(value = "SELECT * FROM messages WHERE chat_id = (:chatId) ORDER BY id DESC LIMIT 1")
    suspend fun getLastMessage(chatId: Int): MessageEntity

    @Query(value = "DELETE FROM messages WHERE chat_id = (:chatId)")
    suspend fun deleteMessagesWithChatId(chatId: Int)
}