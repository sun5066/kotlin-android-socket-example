package github.sun5066.socketclient.network

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import github.sun5066.socketclient.adapter.ChatNavigator
import github.sun5066.socketclient.model.ChatData
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

/***************************************************************************************************
 * @작성자 김민석
 * @작성일 2021-01-04
 * @설명 소켓으로 서버랑 통신 핸들링했지만, ChatViewModel 으로 이동
 * @최종수정일 2021-01-06
 **************************************************************************************************/
//@Deprecated("기능 전부 ChatViewModel 로 이동.")
class ChatSocketHandler : ChatNavigator {
    /**********************************************************************************************/
    private val TAG = this.javaClass.simpleName

    private lateinit var mSocket: Socket
    private lateinit var mReader: Scanner
    private lateinit var mWriter: OutputStream
    private var mIsConnected = false

    private val mGson by lazy { GsonBuilder().create() }
    private val mListType: TypeToken<MutableList<ChatData>> by lazy {
        // 싱글톤은 처음 사용할때 생성하는게 나을꺼같다.
        object : TypeToken<MutableList<ChatData>>() {}
    }

    private val mChatList: MutableList<ChatData> = mutableListOf()
    private val mChatLiveData: MutableLiveData<MutableList<ChatData>> = MutableLiveData()

    companion object {
        private var instance: ChatSocketHandler? = null

        @JvmStatic
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ChatSocketHandler().also { instance = it }
        }
    }

    /**********************************************************************************************/

    override fun getData(): MutableLiveData<MutableList<ChatData>> = mChatLiveData

    override fun connect(_ip: String, _port: Int) {
        mSocket = Socket(_ip, _port)
        mReader = Scanner(mSocket.getInputStream())
        mWriter = mSocket.getOutputStream()
        mIsConnected = true
    }

    override fun read() {
        while (mIsConnected) {
            val tempList: MutableList<ChatData> = mGson.fromJson(
                mReader.nextLine().toString(),
                mListType.type
            )
            mChatList.clear()
            mChatList.addAll(tempList)
            mChatLiveData.postValue(mChatList)
        }
    }

    override fun close() {
        mIsConnected = false
        mReader.close()
        mWriter.close()
        mSocket.close()
    }

    override fun sendMessage(msg: String) {
        if (mIsConnected) {
            mWriter.write((msg + '\n').toByteArray(Charset.defaultCharset()))
        }
    }
}