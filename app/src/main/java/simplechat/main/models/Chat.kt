package simplechat.main.models

data class Chat(val id: Int, var userName: String, var userPhoto: String, var isNewMessageContain: Boolean,
    var lastMessageDate: String)