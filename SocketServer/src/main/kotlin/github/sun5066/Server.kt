package github.sun5066

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import github.sun5066.model.ChatData
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread

fun main() {
    val serverSocket = ServerSocket(1004)
    println("서버 On - Port: ${serverSocket.localPort}")

    while (true) {
        val client = serverSocket.accept()
        println("${client.localAddress} Connect!")

        thread { ClientHandler(client).run() }
    }
}

class ClientHandler(private val client: Socket) {
    private val mListType: TypeToken<MutableList<ChatData>> = object :
        TypeToken<MutableList<ChatData>>() {}

    private val mGson = GsonBuilder().create()
    private val mReader = Scanner(client.getInputStream())
    private val mWriter = client.getOutputStream()
    private val mChatList = mutableListOf<ChatData>()
    private var mSendCount = 0
    private var mReceiveCount = 0

    fun run() {
        thread { disconnected() }
        thread { send() }

        while (true) {
            try {
                val data = mReader.nextLine()
                val isToil = mReceiveCount == 0
                mSendCount = 0
                println(data)

                mChatList.add(
                    ChatData(
                        id = mChatList.size,
                        name = "client",
                        message = data,
                        isClient = true,
                        isToil = isToil
                    )
                )
                this.getList()
            } catch (ex: Exception) {
                client.close()
                break
            }
        }
    }

    private fun disconnected() {
        while (true) {
            if (client.isClosed) {
                println("${client.localAddress} : disconnected!")
                mReader.close()
                mWriter.close()
                client.close()
                break
            }
        }
    }

    private fun send() {
        val scanner = Scanner(System.`in`)
        while (true) {
            val msg = scanner.nextLine()
            val isToil = mSendCount == 0
            mReceiveCount = 0

            mChatList.add(
                ChatData(
                    id = mChatList.size,
                    name = "server",
                    message = msg.toString(),
                    isClient = false,
                    isToil = isToil
                )
            )
            mSendCount++
            this.getList()
        }
    }

    @Deprecated("거의 안쓰는거같은데?")
    private fun write(message: String) {
        mWriter.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }

    private fun getList() {
        val data = mGson.toJson(mChatList, mListType.type)
        try {
            mWriter.write((data + '\n').toByteArray(Charset.defaultCharset()))
        } catch (e: SocketException) {
            client.close()
            println("접속이 끊겼습니다!")
        }
        println(data)
    }
}