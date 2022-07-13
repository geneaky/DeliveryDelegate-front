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

        mSocket = SocketApplication.get()
        val connect = mSocket.connect() // 소켓 연결

        val objectmapper = ObjectMapper()
        try {
            // 게임 방장 생성 후 참가
            val message = Message()
            message.room_name = room_name
            connect.emit("attendMaster", objectmapper.writeValueAsString(message))
        } catch (e: JSONException) {
            e.printStackTrace()
        }


        // 게임 방장 생성 후 참가
        connect.on("attend", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
        })
    }

    // 소켓 연결 해제
    override fun onDestroy() {
        super.onDestroy()
        mSocket.emit("game_end")
        mSocket.disconnect()
//        mSocket.off("EVENT_NAME", '리스너 익명구현 객체')
    }

}