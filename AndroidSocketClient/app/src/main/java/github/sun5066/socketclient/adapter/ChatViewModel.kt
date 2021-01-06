package github.sun5066.socketclient.adapter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import github.sun5066.socketclient.model.ChatData
import github.sun5066.socketclient.network.ChatSocketHandler

/***************************************************************************************************
 * @작성자 sun5066(김민석)
 * @작성일 2021-01-05
 * @설명 소켓으로 서버와 주고받은 데이터 LiveData 로 변환
 * @최종수정일 2021-01-05
 **************************************************************************************************/
class ChatViewModel : ViewModel() {

    private val TAG = this.javaClass.simpleName

    private val mChatLiveData: MutableLiveData<MutableList<ChatData>> = MutableLiveData()

    init {
        mChatLiveData.value = mutableListOf()
    }

    fun getData(): MutableLiveData<MutableList<ChatData>> {
        return ChatSocketHandler.getInstance().getData()
    }
}