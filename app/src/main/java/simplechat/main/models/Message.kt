package simplechat.main.models

data class Message(val id: Int, val chatId: Int, val message: String, var createdAt: String, val typedUserId: Int,
    val messageType: Int, var isDateMustShow: Boolean, var day: String, var time: String)