package github.sun5066.socketclient.adapter

import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
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
class ChatViewModel : ViewModel() {
    private val TAG = this.javaClass.simpleName

    val clickListener = View.OnClickListener { _view ->
        when (_view.id) {
            R.id.txt_me_message -> {
                Toast.makeText(_view.context, "ㅎㅇㅎㅇ", Toast.LENGTH_SHORT).show()
            }
            R.id.txt_target_message -> {
                Toast.makeText(_view.context, "ㅎㅇㅎㅇ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    var m_btnText: ObservableField<String> = ObservableField("전송!")
    var mChatLiveData: LiveData<MutableList<ChatData>> = ChatSocketHelper.gChatLiveData
}