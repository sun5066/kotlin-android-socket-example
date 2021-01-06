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
    private val listType: TypeToken<MutableList<ChatData>> = object :
        TypeToken<MutableList<ChatData>>() {}

    private val gson = GsonBuilder().create()
    private val reader = Scanner(client.getInputStream())
    private val writer = client.getOutputStream()
    private val chatList = mutableListOf<ChatData>()

    fun run() {
        thread { disconnected() }
        thread { send() }

        while (true) {
            try {
                val data = reader.nextLine()
                println(data)

                chatList.add(
                    ChatData(
                        id = chatList.size,
                        name = "client",
                        message = data,
                        isClient = true
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
        if (!client.isConnected) {
            println("${client.localAddress} : disconnected!")
            reader.close()
            writer.close()
            client.close()
        }
    }

    private fun send() {
        val scanner = Scanner(System.`in`)
        while (true) {
            val msg = scanner.nextLine()
            chatList.add(
                ChatData(
                    id = chatList.size,
                    name = "server",
                    message = msg.toString(),
                    isClient = false
                )
            )
            this.getList()
        }
    }

    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }

    private fun getList() {
        val data = gson.toJson(chatList, listType.type)
        try {
            writer.write((data + '\n').toByteArray(Charset.defaultCharset()))
        } catch (e: SocketException) {
            client.close()
            println("접속이 끊겼습니다!")
        }
        println(data)
    }
}