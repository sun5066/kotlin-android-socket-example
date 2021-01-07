package github.sun5066.socketclient.model

data class ChatData(
    val id: Int? = 0,
    var name: String? = "",
    var message: String? = "",
    var isClient: Boolean? = false,
    var isToil: Boolean? = false
)