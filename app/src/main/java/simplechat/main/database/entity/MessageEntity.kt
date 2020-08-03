package simplechat.main.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(@PrimaryKey(autoGenerate = true) var id: Int = 0, @ColumnInfo(name = "chat_id") var chatId: Int = 0,
    @ColumnInfo(name = "message") var message: String = "", @ColumnInfo(name = "created_at") var createdAt: String = "",
    @ColumnInfo(name = "typed_user_id") var typedUserId: Int = 0, @ColumnInfo(name = "message_type") var messageType: Int = 0)