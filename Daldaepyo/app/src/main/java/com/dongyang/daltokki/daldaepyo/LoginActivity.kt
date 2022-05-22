package com.dongyang.daltokki.daldaepyo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.databinding.ActivityLoginBinding
import com.dongyang.daltokki.daldaepyo.databinding.ActivityMainBinding
import com.dongyang.daltokki.daldaepyo.retrofit.LoginDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater)}
    val api = UserAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {

            var id = binding.edtPnumber.text.toString()
            var pw = binding.edtPwd.text.toString()

            // 한 칸이라도 입력하지 않았을 경우
            if(id.isBlank() || pw.isBlank()) {
                var dialog = AlertDialog.Builder(this@LoginActivity)
                dialog.setTitle("로그인 에러")
                dialog.setMessage("모두 입력해 주세요").setPositiveButton("확인", null)
                dialog.show()
                return@setOnClickListener
            }

            val data = LoginDto(id, pw)
            api.postLogin(data).enqueue(object:Callback<LoginDto>{
                override fun onResponse(call: Call<LoginDto>, response: Response<LoginDto>) {
                    val result = response.body()
                    Log.d("로그인", "${result}")
                    Log.d("log body", response.body().toString())
                    var intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onFailure(call: Call<LoginDto>, t: Throwable) {
                    Log.e("로그인", "${t.localizedMessage}")
                }
            })
        }


        binding.btnRegister.setOnClickListener{
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



}
