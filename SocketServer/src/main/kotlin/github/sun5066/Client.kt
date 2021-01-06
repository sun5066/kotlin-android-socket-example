package github.sun5066

import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread

/***********************************************************************************************************************
 * @작성자 김민석
 * @작성일 2020-01-04
 * @설명 간단한 소켓 클라이언트인데, 서버랑 연결하려면 안드로이드 클라이언트랑 연결하는게 좋음. 이 파일은 그냥 테스트 파일입니다.
 **********************************************************************************************************************/
fun main() {
    val address = "125.136.6.85"
    val port = 1004

    val handler = ServerHandler(address, port)
    handler.run()
}

@Deprecated("클라이언트는 안드로이드 프로젝트로 사용해야합니다.")
class ServerHandler(address: String, port: Int) {
    private val socket = Socket(address, port)
    private val reader = Scanner(socket.getInputStream())
    private val writer = socket.getOutputStream()

    fun run() {
        thread { read() }
        while (true) {
            val data = readLine() ?: ""
            if ("EXIT" == data) {
                reader.close()
                socket.close()
                break
            }
            write(data)
        }
    }

    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }

    private fun read() {
        while (socket.isConnected)
            println(reader.nextLine())
    }
}