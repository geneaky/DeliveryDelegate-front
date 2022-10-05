package com.dongyang.daltokki.daldaepyo.Game

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.Game.EmitObject.*
import com.dongyang.daltokki.daldaepyo.MainActivity
import com.dongyang.daltokki.daldaepyo.LoginActivity
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

        tv_result1.visibility = View.GONE // 숨기기
        tv_result2.visibility = View.GONE // 숨기기
        arrive.visibility = View.GONE // 숨기기
        quit_game.visibility = View.GONE // 숨기기
        take_quit.visibility = View.GONE // 숨기기
        tv_warning3.visibility = View.GONE //숨기기
        tv_warning4.visibility = View.GONE //숨기기
        tv_warning5.visibility = View.GONE //숨기기

        val pref = getSharedPreferences("pref", 0)
        val tok = pref.getString("token", "")!!
        val nick = pref.getString("nickname", "")!!
        val Gamepref = getSharedPreferences("Gamepref", 0)
        val room_name = Gamepref.getString("room_name", "")!!
        val game_id = Gamepref.getInt("game_id", 0)

        mSocket = SocketApplication.get()
        val connect = mSocket.connect() // 소켓 연결

        val objectmapper = ObjectMapper()
        val handler = Handler(Looper.getMainLooper())

        try {

            // 재참여
            val last_attend = DelegatorRunAway()
            last_attend.token = tok
            last_attend.room_name = room_name
            // 게임 시작
            val on_game = OnGame()
            on_game.token = tok
            on_game.room_name = room_name
            val game_result = GameResult()
            game_result.token = tok
            game_result.game_id = game_id

            connect.emit("on_game", objectmapper.writeValueAsString(on_game))
            connect.emit("game_result", objectmapper.writeValueAsString(game_result))
            connect.emit("last_attend", objectmapper.writeValueAsString(last_attend))

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        // 재참여
        connect.on("last_attend", Emitter.Listener {
            Log.d("LOGG last_attend", "${it[0]}")
        })

        // 게임 시작 결과(랜덤 배치 끝)
        connect.on("on_game", Emitter.Listener {
            Log.d("LOGG on_game", "${it[0]}")
        })
        // 게임 결과
        connect.on("game_result", Emitter.Listener {
            Log.d("LOGG game_result", "${it[0]}")
            var game_result = it[0].toString()

            val OrderList = getSharedPreferences("OrderList", 0)
            val editOrder = OrderList.edit()
            val Orderpref = getSharedPreferences("Orderpref", 0)
            val editOOrder = Orderpref.edit()

            if(game_result == "대표자가 선정되었습니다") { // 예비대표

                handler.postDelayed(Runnable {

                    tv_result2.visibility = View.VISIBLE // 보여주기
                    take_quit.visibility = View.VISIBLE // 보여주기
                    tv_warning3.visibility = View.VISIBLE // 보여주기
                    tv_warning4.visibility = View.VISIBLE // 보여주기

                    take_quit.isEnabled = false // 비활성화
                    take_quit.setBackgroundColor(Color.LTGRAY)

                    val landmark_result = LandmarkResult()
                    landmark_result.store_name = Gamepref.getString("title", "")!!
                    landmark_result.mapy = Gamepref.getString("lat", "")!!
                    landmark_result.mapx = Gamepref.getString("lng", "")!!
                    landmark_result.detail = ""
                    val result = arrayListOf(objectmapper.writeValueAsString(landmark_result)).toString()
                    editOrder.apply()
                    editOrder.putString("result", result)
                    editOrder.commit()
                    editOOrder.apply()
                    editOOrder.putString("detail", "no backpress")
                    editOOrder.commit()

                }, 0)
            } else { // 대표
                handler.postDelayed(Runnable {

                    tv_result1.visibility = View.VISIBLE // 보여주기
                    arrive.visibility = View.VISIBLE // 보여주기
                    quit_game.visibility = View.VISIBLE // 보여주기
                    tv_warning5.visibility = View.VISIBLE // 보여주기

                    quit_game.isEnabled = false // 비활성화
                    quit_game.setBackgroundColor(Color.LTGRAY)

                    editOrder.apply()
                    editOrder.putString("result", game_result)
                    editOrder.commit()

                    editOOrder.apply()
                    editOOrder.remove("detail")
                    editOOrder.commit()

                }, 0)
            }
            val fm = supportFragmentManager
            val mapFragment = fm.findFragmentById(R.id.map_view3) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fm.beginTransaction().add(R.id.map_view3, it).commit()
                }
            mapFragment.getMapAsync(this)
        })


        arrive.setOnClickListener {

            handler.postDelayed(Runnable {
                quit_game.isEnabled = true // 비활성화
                quit_game.setBackgroundColor(Color.WHITE)
            }, 0)

            try {
                // 랜드마크 도착
                val delegator_arrive = DelegatorArrive()
                delegator_arrive.room_name = room_name

                connect.emit("arrive", objectmapper.writeValueAsString(delegator_arrive))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        connect.on("arrive", Emitter.Listener {
            Log.d("LOGG delegator_arrive", "${it[0]}")

            handler.postDelayed(Runnable {

                take_quit.isEnabled = true // 활성화
                take_quit.setBackgroundColor(Color.WHITE)

                var dialog = AlertDialog.Builder(this@GameResultActivity, R.style.MyDialogTheme)
                dialog.setTitle("랜드마크 도착")
                dialog.setMessage("${it[0]}.").setPositiveButton("확인", null)
                dialog.show()
            }, 0)
        })


        // 받았으면 나가기(예비대표의 게임방 나가기)
        take_quit.setOnClickListener {

            val Orderpref = getSharedPreferences("Orderpref", 0)

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

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }


        // 배달완료(대표의 게임방 나가기)
        quit_game.setOnClickListener {

            val Orderpref = getSharedPreferences("Orderpref", 0)

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

        // 게임 나가기(대표가 방을 나갈 때만 방이 폭파됨)
        connect.on("quit_game", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
            val result = it[0].toString()

            if(result == "방이 폭파되었습니다") {
                handler.postDelayed(Runnable {
                    Toast.makeText(this@GameResultActivity, "대표가 배달을 완료하여 방이 삭제됩니다.", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 0)
            } else {
                handler.postDelayed(Runnable {
                    Toast.makeText(this@GameResultActivity, "${it[0]}님이 나갔습니다.", Toast.LENGTH_SHORT).show()
                }, 0)
            }
        })

        // 대표자의 퇴장
        connect.on("delegator_run_away", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
            handler.postDelayed(Runnable {
                Toast.makeText(this@GameResultActivity, "대표자로 뽑혔음에도 퇴장을 하여, 패널티가 부여됩니다.", Toast.LENGTH_SHORT).show()
            }, 0)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        })


        // 게임 완료 시 sharedpreference 모두 삭제하기
    }

    override fun onMapReady(naverMap: NaverMap) {

        val objectmapper = ObjectMapper()

        mSocket = SocketApplication.get()
        val connect = mSocket.connect() // 소켓 연결

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

        connect.on("arrive", Emitter.Listener {
            Log.d("arrive in map", "${it[0]}")
        })
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
        val handler = Handler(Looper.getMainLooper())

        if(detail.isNotEmpty()) { // 참석자(팀원)인 경우
            // super.onBackPressed()
        }
        if (detail.isEmpty()){ // 방장인 경우
            try {
                // 게임 나가기(방 삭제)
                // 방장은 대표자와 같이 1로 설정해두고 방장이 나가면 대표자가 탈주하는 것과 같은 효과를 줌
                val delegator_run_away = DelegatorRunAway()
                delegator_run_away.token = tok
                delegator_run_away.room_name = room_name

                connect.emit("delegator_run_away", objectmapper.writeValueAsString(delegator_run_away))
                val edit_pref = pref.edit()
                edit_pref.apply()
                edit_pref.remove("token")
                edit_pref.commit()

                // 게임 나가기(퇴장)
                val quit_game = QuitGame()
                quit_game.token = tok
                quit_game.nickname = nick
                quit_game.room_name = room_name

                connect.emit("quit_game", objectmapper.writeValueAsString(quit_game))

                val intent = Intent(this@GameResultActivity, LoginActivity::class.java) 
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

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

        // 게임 나가기(마지막 한 명까지 나가면 방이 삭제됨)
        connect.on("quit_game", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
            handler.postDelayed(Runnable {
                Toast.makeText(this@GameResultActivity, "${it[0]}님이 나갔습니다.", Toast.LENGTH_SHORT).show()
            }, 0)
        })

        // 게임 나가기(대표자의 퇴장으로, 게임 삭제)
        connect.on("delegator_run_away", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
            handler.postDelayed(Runnable {
                Toast.makeText(this@GameResultActivity, "대표자로 뽑혔음에도 퇴장을 하여, 패널티가 부여됩니다.", Toast.LENGTH_SHORT).show()
            }, 0)
        })

    }

}