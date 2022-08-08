package com.dongyang.daltokki.daldaepyo

import android.content.DialogInterface
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


        // 방장이 게임 시작 버튼을 누르면 게임이 시작됨(intent로 넘어감)
        // 만약, 방장이 나가면 방장을 다음에 입장한 사람에게 방장자리를 넘겨주기.. 가능..?


// 대표자 탈주
        btn_delegator_run_away.setOnClickListener {
            val run_away = DelegatorRunAway()
            run_away.token = tok
            run_away.room_name = room_name
            connect.emit("delegator_run_away", objectmapper.writeValueAsString(run_away))
            
            
//            var flag = 0
//
//            // 이건 대표자가 결정되면 바로 대표자에게 다이얼로그 보여주기(대표자만!)
//            var dialog = AlertDialog.Builder(this@GameActivity, R.style.MyDialogTheme)
//            dialog.setTitle("대표자 참석여부 확인")
//            // 확인 눌렀을 때의 이벤트 작성하기
//            // 일정 시간 내에 보여주기(초 점점 사라지는 거 보여주기 해야 함_나중에)
//            dialog.setMessage("대표자는 <확인> 버튼을 클릭해 주세요.")
//            dialog.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, id ->
//                Toast.makeText(applicationContext, "대표자 확인이 완료됐습니다.", Toast.LENGTH_SHORT).show()
//            })
//            dialog.show()
//            flag = 1
//
//            if(flag == 1) { // 대표자가 탈주했음
//                val run_away = DelegatorRunAway()
//                run_away.token = tok
//                run_away.room_name = room_name
//                connect.emit("delegator_run_away", objectmapper.writeValueAsString(run_away))
//            }
            
        }
        // 대표자 다시 선정(미완)_잘 작동하지 않음 ..
        btn_delegator_re_ranking.setOnClickListener {
            val re_ranking = DelegatorReRanking()
            re_ranking.token = tok
            re_ranking.game_id = 1
            re_ranking.room_name = room_name
            re_ranking.nickname = "John"
            re_ranking.ranking = 3
            connect.emit("delegator_re_ranking", objectmapper.writeValueAsString(re_ranking))
        }
        // 대표자 랜드마크 도착(현재 위치와 맞는지 확인하는 것이 필요함)
        btn_delegator_arrive.setOnClickListener {
            val arrive = DelegatorArrive()
            arrive.room_name = room_name
            connect.emit("delegator_arrive", objectmapper.writeValueAsString(arrive))
        }
        // 게임 종료(미완)_ 잘 작동하지 않음
        btn_game_end.setOnClickListener {
            connect.emit("game_remove")
        }
        // 게임 삭제(대표자만 가능)_(미완)_잘 작동하지 않음
        // 대표자가 게임을 나갈 때 있어야 하는 기능이 아닐까 싶음
        btn_game_remove.setOnClickListener {
            val remove = GameRemove()
            remove.room_name = room_name
            remove.ranking = 4 // ranking이 length(size)일 때만 대표자임
            connect.emit("game_remove", objectmapper.writeValueAsString(remove))
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
        // 대표자 탈주
        connect.on("delegator_run_away", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
        })
        // 대표자 다시 선정
        connect.on("delegator_re_ranking", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
        })
        // 대표자 랜드마크 도착
        connect.on("delegator_arrive", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
        })
        // 게임 접속 불가(연결 불가)
        connect.on("disconnect", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
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
        val nick = pref.getString("nickname", "")!!
        val Gamepref = getSharedPreferences("Gamepref", 0)
        val room_name = Gamepref.getString("room_name", "")!!

        mSocket = SocketApplication.get()
        val connect = mSocket.connect() // 소켓 연결

        val objectmapper = ObjectMapper()

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

        // 게임 나가기(마지막 한 명까지 나가면 방이 삭제됨)
        connect.on("quit_game", Emitter.Listener {
            Log.d("LOGG", "${it[0]}")
//            Log.d("LOGG", "${it[1]}")
//            Log.d("LOGG", "${it[2]}")
            Toast.makeText(this, "${it[0]}님이 나갔습니다.", Toast.LENGTH_SHORT).show()
//            val count = it[0].toString()
//            Log.d("count", count)
        })


        val intent = Intent(this@GameActivity, MainActivity::class.java) //지금 액티비티에서 다른 액티비티로 이동하는 인텐트 설정
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()

    }

}