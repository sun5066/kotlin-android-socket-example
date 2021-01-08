package github.sun5066.socketclient

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import github.sun5066.socketclient.adapter.ChatRecyclerAdapterV2
import github.sun5066.socketclient.adapter.ChatViewModel
import github.sun5066.socketclient.databinding.ActivityClientBinding
import github.sun5066.socketclient.model.ChatData
import github.sun5066.socketclient.network.ChatSocketHelper
import kotlin.concurrent.thread


class ClientActivity : AppCompatActivity(), View.OnClickListener {
    /**********************************************************************************************/
    private val TAG = this.javaClass.simpleName
    private val CONNECT_PORT = 1004
    private val KEY_IP = "ip"

    private lateinit var mBinding: ActivityClientBinding
    private lateinit var mChatRecyclerAdapter: ChatRecyclerAdapterV2

    private val mChatViewModel: ChatViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(ChatViewModel::class.java)
    }

    /**********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_client) 바인딩 때문에 필요없음.
        mBinding = ActivityClientBinding.inflate(layoutInflater)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(mBinding.root)

        mBinding.lifecycleOwner = this
        mBinding.viewModel = mChatViewModel

        // 메인스레드에서 네트워킹 관련 처리 허용
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // 서버 연결
        val address = intent.getStringExtra(KEY_IP)
        address?.let {
            ChatSocketHelper.connect(address, CONNECT_PORT)
        }

        try {
            thread { ChatSocketHelper.read() }
        } catch (e: Exception) {
            Toast.makeText(this, "예외발생", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "${e.message}")

            ChatSocketHelper.close()
        }
        mBinding.btnSend.setOnClickListener(this)

//        val chatList = mutableListOf<ChatData>()
//        mChatRecyclerAdapter = ChatRecyclerAdapterV2(chatList)
////        mBinding.recyclerView.adapter = mChatRecyclerAdapter
//
//        mChatViewModel.getChatLiveData()?.observe(this, {
//            mChatRecyclerAdapter.setList(it)
//            val layoutManager = LinearLayoutManager(this)
//            Log.d(TAG, "Observable - $it")
//
//            mBinding.recyclerView.layoutManager = layoutManager
//            mBinding.recyclerView.adapter = mChatRecyclerAdapter
//            mBinding.recyclerView.scrollToPosition(mChatRecyclerAdapter.itemCount - 1)
//        })
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        ChatSocketHelper.close()
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        val msg = mBinding.txtSend.text.toString()
        ChatSocketHelper.sendMessage(msg)
        mBinding.txtSend.text = null
    }
}