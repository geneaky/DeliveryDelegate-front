package com.dongyang.daltokki.daldaepyo.Game

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.Game.EmitObject.GameResult
import com.dongyang.daltokki.daldaepyo.Game.EmitObject.OnGame
import com.dongyang.daltokki.daldaepyo.R
import com.fasterxml.jackson.databind.ObjectMapper
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException

class GameResultActivity : AppCompatActivity() {

    lateinit var mSocket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)

        val pref = getSharedPreferences("pref", 0)
        val tok = pref.getString("token", "")!!
        val Gamepref = getSharedPreferences("Gamepref", 0)
        val room_name = Gamepref.getString("room_name", "")!!
        val game_id = Gamepref.getInt("game_id", 0)

        mSocket = SocketApplication.get()
        val connect = mSocket.connect() // 소켓 연결

        val objectmapper = ObjectMapper()

        try {
            // 게임 시작
            val on_game = OnGame()
            on_game.token = tok
            on_game.room_name = room_name
            val game_result = GameResult()
            game_result.token = tok
            game_result.game_id = game_id

            connect.emit("on_game", objectmapper.writeValueAsString(on_game))
            connect.emit("game_result", objectmapper.writeValueAsString(game_result))
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        // 게임 시작 결과(랜덤 배치 끝)
        connect.on("on_game", Emitter.Listener {
            Log.d("LOGG on_game", "${it[0]}")
        })
        // 게임 결과
        connect.on("game_result", Emitter.Listener {
            Log.d("LOGG game_result", "${it[0]}")
        })

        // 게임 완료 시 sharedpreference 모두 삭제하기
    }
}