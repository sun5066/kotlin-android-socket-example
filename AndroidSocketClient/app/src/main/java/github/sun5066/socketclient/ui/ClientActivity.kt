package github.sun5066.socketclient.ui

import android.os.StrictMode
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import github.sun5066.socketclient.R
import github.sun5066.socketclient.adapter.ChatViewModel
import github.sun5066.socketclient.databinding.ActivityClientBinding
import github.sun5066.socketclient.network.ChatSocketHelper
import github.sun5066.socketclient.ui.base.BaseActivity
import kotlin.concurrent.thread

class ClientActivity : BaseActivity<ActivityClientBinding>(), View.OnClickListener {
    /**********************************************************************************************/
    private val TAG = this.javaClass.simpleName
    private val CONNECT_PORT = 1004
    private val KEY_IP = "ip"

    private val mChatSocketHelper: ChatSocketHelper by lazy { ChatSocketHelper.getInstance() }
    private val mChatViewModel: ChatViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(ChatViewModel::class.java)
    }

    /**********************************************************************************************/

    override fun getLayoutResourceId(): Int = R.layout.activity_client

    override fun initDataBinding() {
        mBinding.lifecycleOwner = this
        mBinding.viewModel = mChatViewModel
    }

    override fun initView() {
        // 키보드 UI가 열렸을때 UI를 위로 올림
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        // 메인스레드에서 네트워킹 관련 처리 허용
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // 서버 연결
        val address = intent.getStringExtra(KEY_IP)
        address?.let {
            mChatSocketHelper.connect(address, CONNECT_PORT)
        }

        try {
            thread { mChatSocketHelper.read() }
        } catch (e: Exception) {
            Toast.makeText(this, "예외발생", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "${e.message}")

            mChatSocketHelper.close()
        }
        mBinding.btnSend.setOnClickListener(this)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        mChatSocketHelper.close()
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        val msg = mBinding.txtSend.text.toString()
        mChatSocketHelper.sendMessage(msg)
        mBinding.txtSend.text = null
    }
}