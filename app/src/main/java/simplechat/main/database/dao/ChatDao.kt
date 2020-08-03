package simplechat.main.database.dao

import androidx.room.*
import simplechat.main.database.entity.ChatEntity

@Dao
interface ChatDao {
    @Query("SELECT * from chats ORDER BY last_message_date DESC")
    suspend fun getAllChats(): List<ChatEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chat: ChatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChats(chats: List<ChatEntity>)

    @Query("DELETE FROM chats")
    suspend fun deleteAll()

    @Update
    suspend fun updateChat(chat: ChatEntity)

    @Delete
    suspend fun deleteChat(chat: ChatEntity)
}