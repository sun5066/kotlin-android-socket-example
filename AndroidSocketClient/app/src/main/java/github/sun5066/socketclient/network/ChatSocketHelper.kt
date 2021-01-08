package github.sun5066.socketclient.network

import androidx.databinding.ObservableField
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import github.sun5066.socketclient.model.ChatData
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

/***************************************************************************************************
 * @작성자 김민석
 * @작성일 2021-01-04
 * @설명 소켓으로 데이터 통신 핸들링하는 클래스
 * @최종수정일 2021-01-07
 **************************************************************************************************/
//@Deprecated("기능 전부 ChatViewModel 로 이동.")
object ChatSocketHelper : ChatSocketNavigator {
    /**********************************************************************************************/
    private lateinit var mSocket: Socket
    private lateinit var mReader: Scanner
    private lateinit var mWriter: OutputStream
    private var mIsConnected = false

    private val mGson by lazy { GsonBuilder().create() }
    private val mListType: TypeToken<MutableList<ChatData>> by lazy {
        // 싱글톤 생성은 최초로 사용할때 생성하는게 나을꺼같다.
        object : TypeToken<MutableList<ChatData>>() {}
    }

    private val mChatLiveData: ObservableField<MutableList<ChatData>> =
        ObservableField(mutableListOf())
    public val gChatLiveData: MutableList<ChatData>? get() = mChatLiveData.get()

    /**********************************************************************************************/

    override fun connect(_ip: String, _port: Int) {
        mSocket = Socket(_ip, _port)
        mReader = Scanner(mSocket.getInputStream())
        mWriter = mSocket.getOutputStream()
        mIsConnected = true
    }

    override fun read() {
        if (::mReader.isInitialized) {
            while (mIsConnected) {
                val tempList: MutableList<ChatData> = mGson.fromJson(
                    mReader.nextLine().toString(),
                    mListType.type
                )
                mChatLiveData.set(tempList)
            }
        }
    }

    override fun close() {
        mIsConnected = false
        mReader.close()
        mWriter.close()
        mSocket.close()
    }

    override fun sendMessage(msg: String) {
        if (::mWriter.isInitialized && mIsConnected) {
            mWriter.write((msg + '\n').toByteArray(Charset.defaultCharset()))
        }
    }
}