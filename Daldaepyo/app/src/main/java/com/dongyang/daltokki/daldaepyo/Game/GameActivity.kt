package com.dongyang.daltokki.daldaepyo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.Game.SocketApplication
import com.dongyang.daltokki.daldaepyo.R
import com.dongyang.daltokki.daldaepyo.retrofit.OrderDto
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_game.*
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException


// socket.io 참조1: https://velog.io/@tera_geniel/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9Ckotlin%EC%99%80-nodejs-socket.io%EB%A1%9C-%ED%86%B5%EC%8B%A0%ED%95%98%EA%B8%B0
// socket.io 참조2: https://github.com/jinusong/Android-Socket
// https://dev-juyoung.github.io/2017/09/05/android-socket-io/

class GameActivity : AppCompatActivity() {

    lateinit var mSocket: Socket
//    private lateinit var client: OkHttpClient
//    private lateinit var jsonObject: JSONObject
//    companion object {
//        private const val base_uurl = "ws://146.56.132.245:8080"
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val pref = getSharedPreferences("pref", 0)
        val tok = pref.getString("token", "")!!
        val Gamepref = getSharedPreferences("Gamepref", 0)
        val room_name = Gamepref.getString("room_name", "")!!
        val mapx = Gamepref.getString("lng", "")!!
        val mapy = Gamepref.getString("lat", "")!!
        val population = Gamepref.getString("Population", "")?.toInt()!!
        val data = OrderDto("subway", mapx, mapy, "주문번호")

        try {
            mSocket = SocketApplication.get()
            mSocket.connect() // 소켓 연결
        } catch (e: URISyntaxException) {
            Log.d("Errr", e.toString())
        }


        btn_ggg.setOnClickListener{
            mSocket.emit("attend", "hello") // 서버 측에 이벤트 송신
        }

        mSocket.on("get message", onConnect) // 서버 측의 메시지를 받음
        // 인원 초과, 게임결과(대표자 닉네임), 대표자가 탈주한 경우
        // 대표자가 랜드마크에 도착한 경우


    }

    // 서버로부터 전달받은 Socket.EVENT_CONNECT 이벤트 처리
    private val onConnect = Emitter.Listener {
        // your code...
    }
}