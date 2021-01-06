package github.sun5066.socketclient.adapter

import androidx.lifecycle.MutableLiveData
import github.sun5066.socketclient.model.ChatData

interface ChatNavigator {
    fun getData(): MutableLiveData<MutableList<ChatData>>

    fun connect(_ip: String, _port: Int)

    fun read()

    fun close()

    fun sendMessage(msg: String)
}