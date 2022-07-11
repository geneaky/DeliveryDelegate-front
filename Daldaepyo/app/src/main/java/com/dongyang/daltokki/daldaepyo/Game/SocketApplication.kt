package com.dongyang.daltokki.daldaepyo.Game

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class SocketApplication {
    companion object {
        // 소켓 선언 및 초기화
        private lateinit var socket : Socket
        fun get(): Socket {
            try {
                // [uri]부분은 "http://X.X.X.X:3000" 꼴로 넣어주는 게 좋다.
                socket = IO.socket("http://146.56.132.245:8080")
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
            return socket
        }
    }
}