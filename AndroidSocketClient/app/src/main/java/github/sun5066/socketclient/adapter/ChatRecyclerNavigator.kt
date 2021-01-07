package github.sun5066.socketclient.adapter

import github.sun5066.socketclient.model.ChatData

interface ChatRecyclerNavigator {
    fun setList(mChatList: MutableList<ChatData>)
}