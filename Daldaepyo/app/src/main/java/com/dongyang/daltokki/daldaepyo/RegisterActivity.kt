package com.dongyang.daltokki.daldaepyo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.databinding.ActivityMainBinding
import com.dongyang.daltokki.daldaepyo.retrofit.LoginDto
import com.dongyang.daltokki.daldaepyo.retrofit.RegisterDto
import com.dongyang.daltokki.daldaepyo.retrofit.SingupDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val api = UserAPI.create();
    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        setContentView(binding.root)

        // 1. phone 중복확인
        // 2. phone 인증
        // 3. 빈 칸이 없도록
        // 4. 비밀번호 재확인? 이런 거 안해도 되낭

        binding.btn_register.setOnClickListener {



            val data = RegisterDto(binding.phone_number.text.toString(),
                                 binding.password.text.toString(),
                                 binding.name.text.toString())
            api.postRegister(data).enqueue(object : Callback<RegisterDto> {
                override fun onResponse(call: Call<RegisterDto>, response: Response<RegisterDto>) {
                    Log.d("log", response.toString())
                    Log.d("log body", response.body().toString())

                    if (!response.body().toString().isEmpty())
                        binding.text.setText(response.body().toString())
                }

                override fun onFailure(call: Call<RegisterDto>, t: Throwable) {
                    Log.d("log", t.message.toString())
                    Log.d("log", "fail")

                }
            })
        }


    }
}