package com.dongyang.daltokki.daldaepyo

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
import com.dongyang.daltokki.daldaepyo.Game.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.activity_order_detail.*
import org.json.JSONException


// socket.io 참조1: https://velog.io/@tera_geniel/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9Ckotlin%EC%99%80-nodejs-socket.io%EB%A1%9C-%ED%86%B5%EC%8B%A0%ED%95%98%EA%B8%B0
// socket.io 참조2: https://github.com/jinusong/Android-Socket
// https://dev-juyoung.github.io/2017/09/05/android-socket-io/
// Toast error: https://velog.io/@ssook1222/%EC%98%A4%EB%8A%98%EC%9D%98-%EC%97%90%EB%9F%AC-java.lang.RuntimeException-Cant-toast-on-a-thread-that-has-not-called-Looper.prepare


class GameActivity : AppCompatActivity() {

    lateinit var mSocket: Socket
    val objectmapper = ObjectMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        btn_game_start.visibility = View.GONE // 숨기기
        btn_game_ready.visibility = View.GONE // 숨기기
        please_ready.visibility = View.GONE // 숨기기

        val pref = getSharedPreferences("pref", 0)
        val tok = pref.getString("token", "")!!
        val nick = pref.getString("nickname", "")!!
        val Gamepref = getSharedPreferences("Gamepref", 0)
        val room_name = Gamepref.getString("room_name", "")!!
        val population = Gamepref.getInt("Population", 0)
        val Orderpref = getSharedPreferences("Orderpref", 0)
        val store_name = Orderpref.getString("store_name", "")!!
        val mapx = Orderpref.getString("lng", "")!!
        val mapy = Orderpref.getString("lat", "")!!
        val detail = Orderpref.getString("detail", "")!!

        mSocket = SocketApplication.get()
        val connect = mSocket.connect() // 소켓 연결

        val handler = Handler(Looper.getMainLooper()) // toast 및 dialog를 스레드에서 사용 가능하게 함

        if(detail.isNotEmpty()) { // 참석자는 게임 생성 시 sharedprefernec에 detail을 저장함.

            btn_game_start.visibility = View.GONE // 숨기기
            btn_game_ready.visibility = View.VISIBLE // 보여주기
            please_ready.visibility = View.VISIBLE // 보여주기

            val game_id : Int = Gamepref.getInt("game_id", 0)            

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

                // 게임 참여 인원 확인
                val delegator_list_view = DelegatorRunAway()
                delegator_list_view.token = tok
                delegator_list_view.room_name = room_name
                connect.emit("delegator_list", objectmapper.writeValueAsString(delegator_list_view))


            } catch (e: JSONException) {
                e.printStackTrace()
            }

            btn_game_ready.setOnClickListener { // 게임 준비 버튼 클릭
                try {
                    val ready_game = Ready()
                    ready_game.token = tok
                    ready_game.nickname = nick
                    ready_game.room_name = room_name
                    connect.emit("ready_game", objectmapper.writeValueAsString(ready_game))
                    btn_game_ready.isEnabled = false
                    btn_game_ready.setBackgroundColor(Color.LTGRAY)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

        } else { // 방장은 게임 생성 시 sharedpreference에 detail을 저장하지 않음

            btn_game_start.visibility = View.VISIBLE // 보여주기
            btn_game_ready.visibility = View.GONE // 숨기기
            please_ready.visibility = View.GONE // 숨기기

            try {
                // 게임 방장 생성 후 참가
                val message = AttendMaster()
                message.room_name = room_name
                connect.emit("attendMaster", objectmapper.writeValueAsString(message))

                // 게임 참여 인원 확인
                val delegator_list_view = DelegatorRunAway()
                delegator_list_view.token = tok
                delegator_list_view.room_name = room_name
                connect.emit("delegator_list", objectmapper.writeValueAsString(delegator_list_view))

            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        btn_game_start.setOnClickListener { // 게임 시작 버튼 클릭
            try {
                val check_ready = CheckReady()
                check_ready.token = tok
                check_ready.nickname = nick
                check_ready.room_name = room_name
                connect.emit("check_ready", objectmapper.writeValueAsString(check_ready))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }




        // 게임 참석
        connect.on("attend", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")

            try {
                val delegator_list_view = DelegatorRunAway()
                delegator_list_view.token = tok
                delegator_list_view.room_name = room_name
                connect.emit("delegator_list", objectmapper.writeValueAsString(delegator_list_view))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        })
        // 게임 참석자 명단 확인
        connect.on("delegator_list", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
            
            var delegator_list = it[0].toString()
            Log.d("GameActivity: delegator_list", delegator_list)
            findlist(delegator_list)
        })
        // 인원 초과
        connect.on("population", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
            handler.postDelayed(Runnable {
                var dialog = AlertDialog.Builder(this@GameActivity, R.style.MyDialogTheme)
                dialog.setTitle("입장불가")
                dialog.setMessage("참여 인원을 초과했습니다. 다른 게임을 이용해 주세요.").setPositiveButton("확인", null)
                dialog.show()
            }, 0)
        })
        // 게임 나가기(마지막 한 명까지 나가면 방이 삭제됨)
        connect.on("quit_game", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
            val result = it[0].toString()

            if(result == "방이 폭파되었습니다") {
                handler.postDelayed(Runnable {
                    Toast.makeText(this@GameActivity, "방장이 퇴장하여 방이 폭파되었습니다.", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                }, 0)
            } else {

                try {
                    // 게임 참여 인원 확인
                    val delegator_list_view = DelegatorRunAway()
                    delegator_list_view.token = tok
                    delegator_list_view.room_name = room_name
                    connect.emit("delegator_list", objectmapper.writeValueAsString(delegator_list_view))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                handler.postDelayed(Runnable {
                    Toast.makeText(this@GameActivity, "${it[0]}님이 나갔습니다.", Toast.LENGTH_SHORT).show()
                }, 0)
            }
        })
        // 게임 준비 완료
        connect.on("ready_game", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
        })
        // 팀원들의 게임준비여부 확인
        connect.on("check_ready", Emitter.Listener {
            Log.d("LOGG check_ready", "${it[0]}")
            if (it[0].toString() == "not_ready") {
                handler.postDelayed(Runnable {
                    if(detail.isNotEmpty()) { // 팀원
                        var dialog = AlertDialog.Builder(this@GameActivity, R.style.MyDialogTheme)
                        dialog.setTitle("게임시작불가")
                        dialog.setMessage("게임 준비를 클릭해주세요.").setPositiveButton("확인", null)
                        dialog.show()
                    } else { // 방장
                        var dialog = AlertDialog.Builder(this@GameActivity, R.style.MyDialogTheme)
                        dialog.setTitle("게임시작불가")
                        dialog.setMessage("모든 사용자의 게임 준비 완료가 필요합니다.").setPositiveButton("확인", null)
                        dialog.show()
                    }
                }, 0)
            }
            if (it[0].toString() == "complete_ready") {
                val intent = Intent(this, GameRandomActivity::class.java) // 게임시작을 클릭하면 주사위가 돈다.
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        })
        // 대표자 탈주
        connect.on("delegator_run_away", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
            handler.postDelayed(Runnable {
                Toast.makeText(this@GameActivity, "방장이 퇴장하여 방이 폭파되었습니다.", Toast.LENGTH_SHORT).show()
            }, 0)
        })
        // 게임 접속 불가(연결 불가)
        connect.on("disconnect", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
            // sharedpreference 삭제
            val editOrder = Orderpref.edit()
            editOrder.apply()
            editOrder.remove("detail")
            editOrder.commit()
        })

    }

    // 게임 참여자 리스트 확인
    fun findlist(delegator_list: String) {

        runOnUiThread {

            val readNick: List<String> = objectmapper.readValue(delegator_list)
            val nick_size = readNick.size
            Log.d("Game: size", nick_size.toString())


            val list = mutableListOf<GameDelegatorListItem>()

            for(i in 0 until nick_size!!) {
                val attender = readNick[i]
                Log.d("Game: attender", attender)
                list.add(GameDelegatorListItem(attender))
            }

            val adapter = GameDelegatorListAdapter(this, list)
            delegator_list_view.adapter = adapter
        }
    }


    // 소켓 연결 해제
    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
    }

    override fun onBackPressed() {

        val pref = getSharedPreferences("pref", 0)
        val tok = pref.getString("token", "")!!
        val nick = pref.getString("nickname", "")!!
        val Gamepref = getSharedPreferences("Gamepref", 0)
        val room_name = Gamepref.getString("room_name", "")!!
        val Orderpref = getSharedPreferences("Orderpref", 0)

        mSocket = SocketApplication.get()
        val connect = mSocket.connect() // 소켓 연결

        val handler = Handler(Looper.getMainLooper()) // toast 및 dialog를 스레드에서 사용 가능하게 함

        try {
            // 게임 나가기(퇴장)
            val quit_game = QuitGame()
            quit_game.token = tok
            quit_game.nickname = nick
            quit_game.room_name = room_name

            connect.emit("quit_game", objectmapper.writeValueAsString(quit_game))
        } catch (e: JSONException) {
            e.printStackTrace()
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


        // 게임 나가기(방장이 방을 나갈 때만 방이 폭파됨)
        connect.on("quit_game", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
            val result = it[0].toString()

            if(result == "방이 폭파되었습니다") {
                handler.postDelayed(Runnable {
                    Toast.makeText(this@GameActivity, "방장이 퇴장하여 방이 폭파되었습니다.", Toast.LENGTH_SHORT).show()
                }, 0)
            } else {
                handler.postDelayed(Runnable {
                    Toast.makeText(this@GameActivity, "${it[0]}님이 나갔습니다.", Toast.LENGTH_SHORT).show()
                }, 0)
            }
        })
    }

}