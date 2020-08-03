package simplechat.main.database.mappers

import simplechat.main.database.entity.ChatEntity
import simplechat.main.models.Chat

object ChatsMapper {
    fun chatEntityToChat(chatEntity: ChatEntity): Chat {
        return Chat(chatEntity.id, chatEntity.userName, chatEntity.userPhoto, chatEntity.isNewMessageContain,
            chatEntity.lastMessageDate)
    }

    fun chatEntitiesToChats(chatEntities: List<ChatEntity>): ArrayList<Chat> {
        return ArrayList<Chat>().apply {
            for (chatEntity in chatEntities) {
                add(chatEntityToChat(chatEntity))
            }
        }
    }

    fun chatToChatEntity(chat: Chat): ChatEntity {
        return ChatEntity(chat.id, chat.userName, chat.userPhoto, chat.isNewMessageContain, chat.lastMessageDate)
    }

    fun chatsToChatEntities(chats: ArrayList<Chat>): ArrayList<ChatEntity> {
        return ArrayList<ChatEntity>().apply {
            for (chat in chats) {
                add(chatToChatEntity(chat))
            }
        }
    }
}