package github.sun5066.socketclient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import github.sun5066.socketclient.databinding.ChatClientItemBinding
import github.sun5066.socketclient.model.ChatData

/***************************************************************************************************
 * 작성자 김민석
 * 작성일 2021-01-07
 * 설명 dataBinding 으로 어댑터 아이템 관리
 **************************************************************************************************/
class ChatRecyclerAdapterV2(private var mChatList: MutableList<ChatData>) :
    RecyclerView.Adapter<ChatRecyclerAdapterV2.ChatHolder?>() {

    fun setList(mChatList: MutableList<ChatData>) {
        this.mChatList = mChatList
    }

    override fun onCreateViewHolder(_parent: ViewGroup, _viewType: Int): ChatHolder {
        val layoutInflater = LayoutInflater.from(_parent.context)
        val chatClientItemBinding = ChatClientItemBinding.inflate(layoutInflater, _parent, false)
        return ChatHolder(chatClientItemBinding)
    }

    override fun onBindViewHolder(_holder: ChatHolder, _position: Int) {
        val chatData = mChatList[_position]
        _holder.gChatClientItemBinding.chatItem = chatData
        _holder.gChatClientItemBinding.executePendingBindings()
    }

    override fun getItemCount(): Int = mChatList.size

    inner class ChatHolder(_chatClientItemBinding: ChatClientItemBinding) :
        RecyclerView.ViewHolder(_chatClientItemBinding.root) {

        val gChatClientItemBinding = _chatClientItemBinding
    }
}