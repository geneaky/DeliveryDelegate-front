package com.dongyang.daltokki.daldaepyo.Game

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.Game.EmitObject.*
import com.dongyang.daltokki.daldaepyo.MainActivity
import com.dongyang.daltokki.daldaepyo.R
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_game_result.*
import org.json.JSONException

class GameResultActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mSocket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)

        tv_result.visibility = View.GONE // 숨기기

        val pref = getSharedPreferences("pref", 0)
        val tok = pref.getString("token", "")!!
        val Gamepref = getSharedPreferences("Gamepref", 0)
        val room_name = Gamepref.getString("room_name", "")!!
        val game_id = Gamepref.getInt("game_id", 0)

        mSocket = SocketApplication.get()
        val connect = mSocket.connect() // 소켓 연결

        val objectmapper = ObjectMapper()
        val handler = Handler(Looper.getMainLooper())

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

            if(game_result == "대표자가 선정되었습니다") {
                handler.postDelayed(Runnable {
                    tv_result.visibility = View.VISIBLE // 보여주기
                    tv_result.text = it[0].toString()
                    Toast.makeText(this@GameResultActivity, "게임 결과, ${it[0]}", Toast.LENGTH_SHORT).show()
                }, 0)
            } else {
                handler.postDelayed(Runnable {

                    Toast.makeText(this@GameResultActivity, "게임 결과, ${it[0]}", Toast.LENGTH_SHORT).show()

                    val OrderList = getSharedPreferences("OrderList", 0)
                    val editOrder = OrderList.edit()
                    editOrder.apply()
                    editOrder.putString("result", game_result)
                    editOrder.commit()

                }, 0)

                val fm = supportFragmentManager
                val mapFragment = fm.findFragmentById(R.id.map_view2) as MapFragment?
                        ?: MapFragment.newInstance().also {
                            fm.beginTransaction().add(R.id.map_view2, it).commit()
                        }
                mapFragment.getMapAsync(this)
            }
        })

        // 게임 완료 시 sharedpreference 모두 삭제하기
    }

    override fun onMapReady(naverMap: NaverMap) {

        val objectmapper = ObjectMapper()

        val OrderList = getSharedPreferences("OrderList", 0)
        val game_result = OrderList.getString("result", "")!!
        Log.d("map_game_result", game_result)

        val readSize2: List<ShowGameResult> = objectmapper.readValue(game_result)
        val order_size = readSize2.size
        Log.d("order_size", order_size.toString())

        for (i in 0 until order_size!!) {
            val marker = Marker()

            val store_name = readSize2[i].store_name
            val mapx: Double = readSize2[i].mapx.toDouble()
            val mapy: Double = readSize2[i].mapy.toDouble()
            val detail = readSize2[i].detail

            Log.d("store_name", store_name)
            Log.d("mapx", mapx.toString())
            Log.d("mapy", mapy.toString())
            Log.d("detail", detail)

            var camPos = CameraPosition(LatLng(mapy, mapx), 17.0)
            naverMap.cameraPosition = camPos

            // 마커찍기
            marker.position = LatLng(mapy, mapx) // 마커
            marker.captionText = store_name + "\n" + detail // 마커에 텍스트 찍기
            marker.map = naverMap

        }
    }

    override fun onBackPressed() {

        val pref = getSharedPreferences("pref", 0)
        val tok = pref.getString("token", "")!!
        val nick = pref.getString("nickname", "")!!
        val Gamepref = getSharedPreferences("Gamepref", 0)
        val room_name = Gamepref.getString("room_name", "")!!
        val Orderpref = getSharedPreferences("Orderpref", 0)
        val detail = Orderpref.getString("detail", "")!!

        mSocket = SocketApplication.get()
        val connect = mSocket.connect() // 소켓 연결

        val objectmapper = ObjectMapper()

        if(detail.isNotEmpty()) { // 참석자(팀원)인 경우
            try {
                // 게임 나가기(퇴장)
                val quit_game = QuitGame()
                quit_game.token = tok
                quit_game.nickname = nick
                quit_game.room_name = room_name

                connect.emit("quit_game", objectmapper.writeValueAsString(quit_game))

                // sharedpreference 삭제
                val editOrder = Orderpref.edit()
                editOrder.apply()
                editOrder.remove("detail")
                editOrder.remove("store_name")
                editOrder.remove("mapy")
                editOrder.remove("mapx")
                editOrder.commit()
                val editGame = Gamepref.edit()
                editGame.apply()
                editGame.remove("room_name")
                editGame.remove("population")
                editGame.commit()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        if (detail.isEmpty()){ // 방장인 경우
            try {
                // 게임 나가기(방 삭제)
                // 방장은 대표자와 같이 1로 설정해두고 방장이 나가면 대표자가 탈주하는 것과 같은 효과를 줌
                val delegator_run_away = DelegatorRunAway()
                delegator_run_away.token = tok
                delegator_run_away.room_name = room_name

                connect.emit("delegator_run_away", objectmapper.writeValueAsString(delegator_run_away))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }


        // 게임 나가기(마지막 한 명까지 나가면 방이 삭제됨)
        connect.on("quit_game", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed(Runnable {
                Toast.makeText(this@GameResultActivity, "${it[0]}님이 나갔습니다.", Toast.LENGTH_SHORT).show()
            }, 0)
        })

        // 게임 나가기(대표자의 퇴장으로, 게임 삭제)
        connect.on("delegator_run_away", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed(Runnable {
                Toast.makeText(this@GameResultActivity, "대표자로 뽑혔음에도 퇴장을 하여, 패널티가 부여됩니다.", Toast.LENGTH_SHORT).show()
            }, 0)
        })


        val intent = Intent(this@GameResultActivity, MainActivity::class.java) //지금 액티비티에서 다른 액티비티로 이동하는 인텐트 설정
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()

    }

}