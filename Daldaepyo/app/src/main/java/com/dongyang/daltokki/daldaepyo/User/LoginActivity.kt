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
import com.dongyang.daltokki.daldaepyo.retofit.LoginInfoResponseDto
import com.dongyang.daltokki.daldaepyo.retrofit.LoginDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {


    val TAG = "로그으으으@@@@@"
    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater)}
    val api = UserAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pref = getSharedPreferences("pref", 0)
        val savedId = pref.getString("id", "").toString()
        Log.d(TAG, savedId)
        val tok = pref.getString("token", "")
        Log.d("getString@@@@@@@@@", "" + tok)

        if(savedId.isNotEmpty()) {
            var intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {

            var id = binding.edtPnumber.text.toString()
            var pw = binding.edtPwd.text.toString()

            // 한 칸이라도 입력하지 않았을 경우
            if(id.isBlank() || pw.isBlank()) {
                var dialog = AlertDialog.Builder(this@LoginActivity, R.style.MyDialogTheme)
                dialog.setTitle("로그인 에러")
                dialog.setMessage("모두 입력해 주세요").setPositiveButton("확인", null)
                dialog.show()
                return@setOnClickListener
            }

            val data = LoginDto(id, pw)
            api.postLogin(data).enqueue(object:Callback<LoginInfoResponseDto>{
                override fun onResponse(call: Call<LoginInfoResponseDto>, response: Response<LoginInfoResponseDto>) {
                    val result = response.body()
                    Log.d("로그인", "${result}")
                    Log.d("log body", response.body().toString())
                    val code = response.code()

                    if(code == 400) { // UnAuthorized User Request
                        var dialog = AlertDialog.Builder(this@LoginActivity, R.style.MyDialogTheme)
                        dialog.setTitle("로그인 에러")
                        dialog.setMessage("로그인 정보를 확인해 주세요.").setPositiveButton("확인", null)
                        dialog.show()
                        return
                    } else if(code == 200) { // success
                        val token_result = response?.body()?.token?.token.toString()
                        Log.d("token_result@@@", "" + token_result)

                        val edit = pref.edit() // 수정모드
                        edit.apply()
                        edit.putString("id", id)
                        edit.putString("token", token_result)
                        edit.commit()

                        val tok = pref.getString("token", "")
                        Log.d("getString@@@@@@@@@", "" + tok)


                        var intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onFailure(call: Call<LoginInfoResponseDto>, t: Throwable) {
                    Log.e("로그인", "${t.localizedMessage}")
                }
            })
        }


        binding.btnRegister.setOnClickListener{
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }



}
