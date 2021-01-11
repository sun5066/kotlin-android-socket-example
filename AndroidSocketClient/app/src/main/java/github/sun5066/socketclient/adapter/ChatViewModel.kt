package github.sun5066.socketclient.adapter

import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import github.sun5066.socketclient.R
import github.sun5066.socketclient.model.ChatData
import github.sun5066.socketclient.network.ChatSocketHelper

/***************************************************************************************************
 * @작성자 sun5066(김민석)
 * @작성일 2021-01-05
 * @설명 소켓으로 서버와 주고받은 데이터 LiveData 로 ClientActivity 가 관찰할 수 있게 해주는 클래스
 * @최종수정일 2021-01-06
 **************************************************************************************************/
class ChatViewModel : ViewModel(),
    ChatSocketHelper.ChatHelperListener {

    private val TAG = this.javaClass.simpleName

    private val mChatSocketHelper = ChatSocketHelper.getInstance()
    var mButtonText: ObservableField<String> = ObservableField("전송!")
    var mChatLiveData: MutableLiveData<MutableList<ChatData>> = MutableLiveData()

    init {
        mChatSocketHelper.setListener(this)
    }

    val mClickListener = View.OnClickListener { _view ->
        Log.d(TAG, "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")
        when (_view.id) {
            R.id.txt_me_message -> {
                Log.d(TAG, "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")
            }
            R.id.txt_target_message -> {
                Log.d(TAG, "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")
            }
        }
    }

    override fun onChatMessage(_data: MutableList<ChatData>) {
        mChatLiveData.postValue(_data)
    }
}