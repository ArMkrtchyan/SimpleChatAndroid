package simplechat.main.database.mappers

import simplechat.main.database.entity.MessageEntity
import simplechat.main.models.Message

object MessagesMapper {
    fun messageEntityToMessage(messageEntity: MessageEntity): Message {
        return Message(messageEntity.id, messageEntity.chatId, messageEntity.message, messageEntity.createdAt,
            messageEntity.typedUserId, messageEntity.messageType, false, "", "")
    }

    fun messageEntitiesToMessages(messageEntities: List<MessageEntity>): ArrayList<Message> {
        return ArrayList<Message>().apply {
            for (messageEntity in messageEntities) {
                add(messageEntityToMessage(messageEntity))
            }
        }
    }

    fun messageToMessageEntity(message: Message): MessageEntity {
        return MessageEntity(message.id, message.chatId, message.message, message.createdAt, message.typedUserId, message.messageType)
    }

    fun messagesToMessageEntities(messages: ArrayList<Message>): ArrayList<MessageEntity> {
        return ArrayList<MessageEntity>().apply {
            for (message in messages) {
                add(messageToMessageEntity(message))
            }
        }
    }
}