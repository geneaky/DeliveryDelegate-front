package com.dongyang.daltokki.daldaepyo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.Game.EmitObject.*
import com.dongyang.daltokki.daldaepyo.Game.SocketApplication
import com.fasterxml.jackson.databind.ObjectMapper
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException


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
        val Orderpref = getSharedPreferences("Orderpref", 0)
        val store_name = Orderpref.getString("store_name", "")!!
        val mapx = Orderpref.getString("lng", "")!!
        val mapy = Orderpref.getString("lat", "")!!
        val detail = Orderpref.getString("detail", "")!!

        mSocket = SocketApplication.get()
        val connect = mSocket.connect() // 소켓 연결

        val objectmapper = ObjectMapper()

        if(detail.isNotEmpty()) { // 참석자는 게임 생성 시 sharedprefernec에 detail을 저장함.

            val population = Gamepref.getString("Population", "")?.toInt()!! // null값이 존재할 수도 있어서 이곳에 넣음

            try {
                // 게임 참석
                val attend_game = AttendGame()
                val order = AttendGame_Order()
                attend_game.token = tok
                attend_game.game_id = 1 // game_id 가 int가 맞는지 물어보기
                attend_game.room_name = room_name
                attend_game.size = population
                order.store_name = store_name
                order.mapx = mapx
                order.mapy = mapy
                order.detail = detail
                attend_game.order = order
                connect.emit("attend", objectmapper.writeValueAsString(attend_game))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            // 게임 참석
            connect.on("attend", Emitter.Listener {
                Log.d("LOGG", "${it[0]}")
            })

        } else { // 방장은 게임 생성 시 sharedpreference에 detail을 저장하지 않음
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

    }

    // 소켓 연결 해제
    override fun onDestroy() {
        super.onDestroy()
//        mSocket.emit("game_end")
        mSocket.disconnect()
//        mSocket.off("EVENT_NAME", '리스너 익명구현 객체')
    }

    override fun onBackPressed() {
        mSocket.emit("game_remove")
        val intent = Intent(this@GameActivity, MainActivity::class.java) //지금 액티비티에서 다른 액티비티로 이동하는 인텐트 설정
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()

    }

}