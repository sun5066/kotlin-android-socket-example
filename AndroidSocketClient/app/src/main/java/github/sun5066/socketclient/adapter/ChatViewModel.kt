package github.sun5066.socketclient.adapter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import github.sun5066.socketclient.model.ChatData
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

/***************************************************************************************************
 * @작성자 sun5066(김민석)
 * @작성일 2021-01-05
 * @설명 소켓으로 서버와 주고받은 데이터 LiveData 로 변환
 * @최종수정일 2021-01-05
 **************************************************************************************************/
class ChatViewModel : ViewModel() {

    /**********************************************************************************************/
    private val TAG = this.javaClass.simpleName

    private lateinit var mSocket: Socket
    private lateinit var mReader: Scanner
    private lateinit var mWriter: OutputStream
    private var mIsConnected = false

    private val mGson = GsonBuilder().create()
    private val mListType: TypeToken<MutableList<ChatData>> = object :
        TypeToken<MutableList<ChatData>>() {}

    private val mChatList: MutableList<ChatData> = mutableListOf()
    private val mChatLiveData: MutableLiveData<MutableList<ChatData>> = MutableLiveData()

    /**********************************************************************************************/
    init {
        mChatLiveData.value = mutableListOf<ChatData>()
    }

    fun getModel(): MutableLiveData<MutableList<ChatData>> {
        return mChatLiveData
    }

    fun run(_ip: String, _port: Int) {
        mSocket = Socket(_ip, _port)
        mReader = Scanner(mSocket.getInputStream())
        mWriter = mSocket.getOutputStream()
        mIsConnected = true
    }

    fun read() {
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

    fun close() {
        mIsConnected = false
        mReader.close()
        mWriter.close()
        mSocket.close()
    }

    fun sendMessage(msg: String) {
        if (mIsConnected) {
            mWriter.write((msg + '\n').toByteArray(Charset.defaultCharset()))
        }
    }
}