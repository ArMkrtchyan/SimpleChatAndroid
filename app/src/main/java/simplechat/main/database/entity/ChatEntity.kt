package simplechat.main.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(@PrimaryKey(autoGenerate = true) var id: Int = 0, @ColumnInfo(name = "user_name") var userName: String = "",
    @ColumnInfo(name = "user_photo") var userPhoto: String = "",
    @ColumnInfo(name = "is_new_message_contain") var isNewMessageContain: Boolean = false,
    @ColumnInfo(name = "last_message_date") var lastMessageDate: String = "")