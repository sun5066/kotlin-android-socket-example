package github.sun5066.socketclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import github.sun5066.socketclient.R
import github.sun5066.socketclient.model.ChatData

class ChatRecyclerAdapter(private var chatList: MutableList<ChatData>) :
    RecyclerView.Adapter<ChatRecyclerAdapter.ChatHolder?>() {

    fun setList(chatList: MutableList<ChatData>) {
        this.chatList = chatList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.chat_client_item, parent, false)
        return ChatHolder(view)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        if (chatList[position].isClient!!) {
            holder.layoutTarget.visibility = View.GONE
            holder.layoutMe.visibility = View.VISIBLE

            holder.textMeMessage.text = chatList[position].message
        } else {
            holder.layoutTarget.visibility = View.VISIBLE
            holder.layoutMe.visibility = View.GONE

            holder.textTargetName.text = chatList[position].name
            holder.textTargetMessage.text = chatList[position].message
        }
    }

    override fun getItemCount(): Int = chatList.size

    class ChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textMeMessage: TextView = itemView.findViewById(R.id.txt_me_message)
        var textTargetName: TextView = itemView.findViewById(R.id.txt_target_name)
        var textTargetMessage: TextView = itemView.findViewById(R.id.txt_target_message)
        var layoutTarget: LinearLayout = itemView.findViewById(R.id.layout_target)
        var layoutMe: LinearLayout = itemView.findViewById(R.id.layout_me)
    }
}