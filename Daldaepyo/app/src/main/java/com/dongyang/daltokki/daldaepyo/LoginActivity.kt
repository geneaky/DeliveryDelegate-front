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
//        loadSplashScreen()
//
        binding.btnLogin.setOnClickListener {
            val data = LoginDto(binding.edtPnumber.text.toString(),
                                binding.edtPwd.text.toString())
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

//        binding.btnLogin.setOnClickListener{
//
//            // 값을 저장할 땐 editor 사용. apply 적용하기..!!
//            // 값을 가져올 땐 pref 사용.
//            val user_pref = this.getPreferences(Context.MODE_PRIVATE) // Context.MODE_PRIVATE 대신 0을 써도됨
//            val user_editor = user_pref.edit()
//
//            var number = binding.edtPnumber.text.toString()
//            var pass = binding.edtPwd.text.toString()
//
//
//            if(number.isNullOrBlank() || pass.isNullOrBlank()) {
//                var dialog = AlertDialog.Builder(this@LoginActivity)
//                dialog.setTitle("로그인 에러")
//                dialog.setMessage("모두 입력해 주세요").setPositiveButton("확인", null)
//                dialog.show()
//            } else {
//                val data = LoginDto(number, pass)
//                api.postLogin(data).enqueue(object: Callback<LoginDto> {
//                    override fun onResponse(call: Call<LoginDto>, response: Response<LoginDto>) {
//                        if(response.isSuccessful) { // 성공적으로 받아왔을 때
////                            if(response.body()!!.success) {
//                            Log.d("log", response.toString())
//                            Log.d("log body", response.body().toString())
//
//                            user_editor.putString("phone_number", number)
//                            user_editor.putString("password", pass)
//                            user_editor.commit()
//
//                            Toast.makeText(this@LoginActivity, "로그인 완료", Toast.LENGTH_SHORT).show()
//
//                            var intent = Intent(this@LoginActivity, MainActivity::class.java)
//                            startActivity(intent)
//                            finish()
////                      }
//
//
//
////                            if (!response.body().toString().isEmpty())
////                                binding.text.setText(response.body().toString())
//                        }
//                        else {
//                            var dialog = AlertDialog.Builder(this@LoginActivity)
//                            dialog.setTitle("로그인 에러")
//                            dialog.setMessage("전화번호, 비밀번호를 확인해 주세요.").setPositiveButton("확인", null)
//                            dialog.show()
//                            return
//                        }
//
//                    }
//
//                    override fun onFailure(call: Call<LoginDto>, t: Throwable) {
//                        Log.d("log", t.message.toString())
//                        Log.d("log", "fail")
//                        return
//                    }
//                })
//            }
//
//
//
//            startActivity(intent)
//        }
//
        binding.btnRegister.setOnClickListener{
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



}
