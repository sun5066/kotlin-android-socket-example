package github.sun5066.socketclient.adapter

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import github.sun5066.socketclient.model.ChatData
import github.sun5066.socketclient.network.ChatSocketHelper

/***************************************************************************************************
 * @작성자 sun5066(김민석)
 * @작성일 2021-01-05
 * @설명 소켓으로 서버와 주고받은 데이터를 LiveData 로 변경후 DataBinding 으로 RecyclerView 에 적용
 * @최종수정일 2021-01-11
 **************************************************************************************************/
class ChatViewModel : ViewModel(),
    ChatSocketHelper.ChatHelperListener {

    private val mChatSocketHelper = ChatSocketHelper.getInstance()
    var mButtonText: ObservableField<String> = ObservableField("전송!")
    var mChatLiveData: MutableLiveData<MutableList<ChatData>> = MutableLiveData()

    init {
        mChatSocketHelper.setListener(this)
    }

    override fun onChatMessage(_data: MutableList<ChatData>) {
        mChatLiveData.postValue(_data)
    }
}