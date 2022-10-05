package com.dongyang.daltokki.daldaepyo.User

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dongyang.daltokki.daldaepyo.R
import com.dongyang.daltokki.daldaepyo.retrofit.ModifyUserDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import com.dongyang.daltokki.daldaepyo.retrofit.UserDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserResponseDto
import kotlinx.android.synthetic.main.activity_user_modify.*
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.fragment_user.tv_user_modify
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class UserModifyActivity  : Activity() {

    val api = UserAPI.create()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_modify)

        val pref = getSharedPreferences("pref", 0)
        val tok = pref.getString("token", "").toString()

        api.getUser(tok).enqueue(object : retrofit2.Callback<UserDto> {
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                val code = response.code()
                Log.d("UserModify", code.toString())

                if(response.isSuccessful){
                    val phone_number = response.body()?.userInfo?.phone_number.toString()
                    val nickname = response.body()?.userInfo?.nickname.toString()

                    tv_user_phone.setText(phone_number)
                    edt_nickname.setText(nickname)
                } else{

                }
            }
            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                Log.e("사용자", "${t.localizedMessage}")
            }
        })


        btn_modifying_user.setOnClickListener{

            val nickname = edt_nickname.text.toString()
            val data = ModifyUserDto(nickname)

            api.postUser(tok, data).enqueue(object : retrofit2.Callback<UserResponseDto> {
                override fun onResponse(
                    call: Call<UserResponseDto>,
                    response: Response<UserResponseDto>
                ) {
                    val message = response.body()?.message.toString()
                    Log.d("message", message)
                    finish()

                }

                override fun onFailure(call: Call<UserResponseDto>, t: Throwable) {
                    Log.d("실패", t.message.toString())
                }
            })


        }

    }

}