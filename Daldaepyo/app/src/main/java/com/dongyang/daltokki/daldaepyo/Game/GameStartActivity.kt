package com.dongyang.daltokki.daldaepyo.Game

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dongyang.daltokki.daldaepyo.Game.EmitObject.*
import com.dongyang.daltokki.daldaepyo.MainActivity
import com.dongyang.daltokki.daldaepyo.R
import com.fasterxml.jackson.databind.ObjectMapper
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_game_start.*
import org.json.JSONException

class GameStartActivity : AppCompatActivity() {

    lateinit var mSocket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_start)

        val pref = getSharedPreferences("pref", 0)
        val tok = pref.getString("token", "")!!
        val Gamepref = getSharedPreferences("Gamepref", 0)
        val room_name = Gamepref.getString("room_name", "")!!

        mSocket = SocketApplication.get()
        val connect = mSocket.connect() // 소켓 연결

        val objectmapper = ObjectMapper()

        Glide.with(this).load(R.raw.random_dice).into(imageView)
        loadSplashScreen()

        try {
            // 게임 시작
            val on_game = OnGame()
            on_game.token = tok
            on_game.room_name = room_name
            connect.emit("on_game", objectmapper.writeValueAsString(on_game))
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        // 게임 시작 결과(랜덤 배치 끝)
        connect.on("on_game", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
        })

        // 게임 완료 시 sharedpreference 모두 삭제하기
    }

    private fun loadSplashScreen(){
        Handler().postDelayed({
            // You can declare your desire activity here to open after finishing splash screen. Like MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}