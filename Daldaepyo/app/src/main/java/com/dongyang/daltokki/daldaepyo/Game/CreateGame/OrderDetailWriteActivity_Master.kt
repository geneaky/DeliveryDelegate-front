package com.dongyang.daltokki.daldaepyo.Game.CreateGame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.MainActivity
import com.dongyang.daltokki.daldaepyo.R
import com.dongyang.daltokki.daldaepyo.retrofit.GameDto
import com.dongyang.daltokki.daldaepyo.retrofit.GameResponseDto
import com.dongyang.daltokki.daldaepyo.retrofit.OrderDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import kotlinx.android.synthetic.main.activity_create_game.*
import kotlinx.android.synthetic.main.activity_order_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailWriteActivity_Master : AppCompatActivity() {

    val api = UserAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        val pref = getSharedPreferences("pref", 0)
        val tok = pref.getString("token", "").toString()

        val Gamepref = getSharedPreferences("Gamepref", 0)
        var gametype = Gamepref.getString("Gametype", "").toString()
        var gamename = Gamepref.getString("Gamename", "").toString()
        var gametext = Gamepref.getString("Gametext", "").toString()
        var population = Gamepref.getString("Population", "").toString().toInt()
        var landmark_name = Gamepref.getString("title", "").toString()
        var landmark_posx = Gamepref.getString("lng", "").toString()
        var landmark_posy = Gamepref.getString("lat", "").toString()

        val Orderpref = getSharedPreferences("Orderpref", 0) // Order 정보
        var store_name = Orderpref.getString("title", "").toString()
        var mapx = Gamepref.getString("lng", "").toString()
        var mapy = Gamepref.getString("lat", "").toString()

        edt_store_name.setText(store_name)
        var detail = edt_detail.text.toString()

        btn_create_game.setOnClickListener {

            val order = OrderDto(store_name, mapx, mapy, detail)
            val data = GameDto(gametype, gamename, gametext, population, landmark_name, landmark_posx, landmark_posy,
                    order) // x: 경도, y: 위도
            api.postCreateGame(tok, data).enqueue(object : Callback<GameResponseDto> {
                override fun onResponse(call: Call<GameResponseDto>, response: Response<GameResponseDto>) {
                    Log.d("log", response.toString())
                    Log.d("log body", response.body().toString())
                    val code = response.code()

                    if(code == 200) {
                        val room_name = response.body()?.name
                        val edit = Gamepref.edit() // 수정모드
                        edit.apply()
                        edit.putString("room_name", room_name)
                        edit.commit()

                    Toast.makeText(this@OrderDetailWriteActivity_Master, "게임방 생성이 완료되었습니다.", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@OrderDetailWriteActivity_Master, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    } else {
                        Toast.makeText(this@OrderDetailWriteActivity_Master, "게임방 생성에 실패했습니다. 관리자에게 문의해 주세요.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<GameResponseDto>, t: Throwable) {
                    Log.d("log", t.message.toString())
                    Log.d("log", "fail")


                }
            })


        }

    }
}