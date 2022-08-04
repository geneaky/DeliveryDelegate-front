package com.dongyang.daltokki.daldaepyo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
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
        val population = Gamepref.getString("Population", "")?.toInt()!!
        val Orderpref = getSharedPreferences("Orderpref", 0)
        val store_name = Orderpref.getString("store_name", "")!!
        val mapx = Orderpref.getString("lng", "")!!
        val mapy = Orderpref.getString("lat", "")!!
        val detail = Orderpref.getString("detail", "")!!

        mSocket = SocketApplication.get()
        val connect = mSocket.connect() // 소켓 연결

        val objectmapper = ObjectMapper()

        if(detail.isNotEmpty()) { // 참석자는 게임 생성 시 sharedprefernec에 detail을 저장함.

            val game_id = Gamepref.getString("game_id", "")?.toInt()!!
            
            try {
                // 게임 참석
                val attend_game = Attend()
                val order = AttendOrder()
                attend_game.token = tok
                attend_game.game_id = game_id
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

        } else { // 방장은 게임 생성 시 sharedpreference에 detail을 저장하지 않음
            try {
                // 게임 방장 생성 후 참가
                val message = AttendMaster()
                message.room_name = room_name
                connect.emit("attendMaster", objectmapper.writeValueAsString(message))
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        // 게임 참석
        connect.on("attend", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
            Log.d("LGGGG", "참여")
        })
        // 인원 초과
        connect.on("population", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
            var dialog = AlertDialog.Builder(this@GameActivity, R.style.MyDialogTheme)
            dialog.setTitle("입장불가")
            dialog.setMessage("참여 인원을 초과했습니다. 다른 게임을 이용해 주세요.").setPositiveButton("확인", null)
            dialog.show()
        })

    }

    // 소켓 연결 해제
    override fun onDestroy() {
        super.onDestroy()
//        mSocket.emit("game_end")
        mSocket.disconnect()
//        mSocket.off("EVENT_NAME", '리스너 익명구현 객체')
    }

    override fun onBackPressed() {

        val pref = getSharedPreferences("pref", 0)
        val tok = pref.getString("token", "")!!
        val Gamepref = getSharedPreferences("Gamepref", 0)
        val room_name = Gamepref.getString("room_name", "")!!

        mSocket = SocketApplication.get()
        val connect = mSocket.connect() // 소켓 연결

        val objectmapper = ObjectMapper()

        try {
            // 게임 나가기(퇴장)
            val quit_game = QuitGame()
            quit_game.token = tok
            quit_game.room_name = room_name

            connect.emit("quit_game", objectmapper.writeValueAsString(quit_game))
        } catch (e: JSONException) {
            e.printStackTrace()
        }


        val intent = Intent(this@GameActivity, MainActivity::class.java) //지금 액티비티에서 다른 액티비티로 이동하는 인텐트 설정
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()

    }

}