package com.dongyang.daltokki.daldaepyo.Game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.GameActivity
import com.dongyang.daltokki.daldaepyo.MainActivity
import com.dongyang.daltokki.daldaepyo.R
import com.dongyang.daltokki.daldaepyo.retrofit.GameDto
import com.dongyang.daltokki.daldaepyo.retrofit.GameResponseDto
import com.dongyang.daltokki.daldaepyo.retrofit.OrderDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import kotlinx.android.synthetic.main.activity_order_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AttendOrderDetailWriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)


        val Orderpref = getSharedPreferences("Orderpref", 0) // Order 정보
        var store_name = Orderpref.getString("title", "").toString()

        edt_store_name.setText(store_name) // AttendOrderActivity에서 선택한 가게명을 보여줌
        var detail = edt_detail.text.toString()

        btn_create_game.setOnClickListener {

            val edit = Orderpref.edit() // 수정모드
            edit.apply()
            edit.putString("detail", detail)
            edit.commit()

            Toast.makeText(this@AttendOrderDetailWriteActivity, "게임에 참여합니다.", Toast.LENGTH_LONG).show()
            val intent = Intent(this@AttendOrderDetailWriteActivity, GameActivity::class.java)
            startActivity(intent)
            finish()


        }

    }
}