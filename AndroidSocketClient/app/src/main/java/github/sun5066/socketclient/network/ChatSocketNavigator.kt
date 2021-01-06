package github.sun5066.socketclient.network

interface ChatSocketNavigator {
    fun connect(_ip: String, _port: Int)

    fun read()

    fun close()

    fun sendMessage(msg: String)
}