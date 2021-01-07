package github.sun5066.socketclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import github.sun5066.socketclient.R
import github.sun5066.socketclient.model.ChatData

/***************************************************************************************************
 * 작성자 김민석
 * 작성일 2021-01-04
 * 설명 dataBinding 안쓰고 사용할때의 어댑터
 * 최종수정일 2021-01-07
 **************************************************************************************************/
@Deprecated("코드 수정을 위한 예비용")
class ChatRecyclerAdapterV1(private var mChatList: MutableList<ChatData>) :
    RecyclerView.Adapter<ChatRecyclerAdapterV1.ChatHolder?>() {

    fun setList(mChatList: MutableList<ChatData>) {
        this.mChatList = mChatList
    }

    fun getAdapter(): RecyclerView.Adapter<ChatRecyclerAdapterV1.ChatHolder?> = this

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.chat_client_item, parent, false)
        return ChatHolder(view)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        if (mChatList[position].isClient!!) {
            holder.layoutTarget.visibility = View.GONE
            holder.layoutMe.visibility = View.VISIBLE

            holder.textMeMessage.text = mChatList[position].message
        } else {
            holder.layoutTarget.visibility = View.VISIBLE
            holder.layoutMe.visibility = View.GONE

            holder.textTargetName.text = mChatList[position].name
            holder.textTargetMessage.text = mChatList[position].message
        }
    }

    override fun getItemCount(): Int = mChatList.size

    class ChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textMeMessage: TextView = itemView.findViewById(R.id.txt_me_message)
        var textTargetName: TextView = itemView.findViewById(R.id.txt_target_name)
        var textTargetMessage: TextView = itemView.findViewById(R.id.txt_target_message)
        var layoutTarget: LinearLayout = itemView.findViewById(R.id.layout_target)
        var layoutMe: LinearLayout = itemView.findViewById(R.id.layout_me)
    }
}