package github.sun5066.model

data class ChatData(
    val id: Int? = 0,
    var name: String? = "",
    var message: String? = "",
    var isClient: Boolean? = false,
    var isTail: Boolean? = false
)