package github.sun5066.socketclient

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import github.sun5066.socketclient.adapter.ChatRecyclerAdapterV1
import github.sun5066.socketclient.adapter.ChatRecyclerAdapterV2
import github.sun5066.socketclient.adapter.ChatRecyclerNavigator
import github.sun5066.socketclient.adapter.ChatViewModel
import github.sun5066.socketclient.databinding.ActivityClientBinding
import github.sun5066.socketclient.model.ChatData
import github.sun5066.socketclient.network.ChatSocketHandler
import github.sun5066.socketclient.network.ChatSocketNavigator
import kotlin.concurrent.thread


class ClientActivity : AppCompatActivity(), View.OnClickListener {
    /**********************************************************************************************/
    private val TAG = this.javaClass.simpleName
    private val CONNECT_PORT = 1004
    private val KEY_IP = "ip"

    private lateinit var mChatRecyclerAdapter: ChatRecyclerNavigator

    private val mChatViewModel: ChatViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(ChatViewModel::class.java)
    }
    private lateinit var mBinding: ActivityClientBinding
    private val mChatSocketHandler: ChatSocketNavigator by lazy { ChatSocketHandler.getInstance() }

    /**********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_client) 바인딩 때문에 필요없음.
        mBinding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.lifecycleOwner = this
        mBinding.viewModel = mChatViewModel

        // 메인스레드에서 네트워킹 관련 처리 허용
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // 서버 연결
        val address = intent.getStringExtra(KEY_IP)
        address?.let {
            mChatSocketHandler.connect(address, CONNECT_PORT)
        }

        try {
            thread { mChatSocketHandler.read() }
        } catch (e: Exception) {
            Toast.makeText(this, "예외발생", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "${e.message}")

            mChatSocketHandler.close()
        }
        mBinding.btnSend.setOnClickListener(this)

        val chatList = mutableListOf<ChatData>()
        mChatRecyclerAdapter = ChatRecyclerAdapterV2(chatList)
//        mBinding.recyclerView.adapter = mChatRecyclerAdapter

        mChatViewModel.getChatLiveData().observe(this, {
            mChatRecyclerAdapter.setList(it)
            val layoutManager = LinearLayoutManager(this)
            Log.d(TAG, "Observable - $it")

            mBinding.recyclerView.layoutManager = layoutManager
            mBinding.recyclerView.adapter = (mChatRecyclerAdapter as ChatRecyclerAdapterV2).getAdapter()
            mBinding.recyclerView.scrollToPosition((mChatRecyclerAdapter as ChatRecyclerAdapterV2).getAdapter().itemCount - 1)
        })
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        mChatSocketHandler.close()
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        val msg = mBinding.txtSend.text.toString()
        mChatSocketHandler.sendMessage(msg)
        mBinding.txtSend.text = null
    }
}