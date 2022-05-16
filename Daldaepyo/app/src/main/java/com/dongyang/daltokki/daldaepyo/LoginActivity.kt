package com.dongyang.daltokki.daldaepyo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    val api = UserAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        loadSplashScreen()



        btn_login.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)

            // 값을 저장할 땐 editor 사용. apply 적용하기..!!
            // 값을 가져올 땐 pref 사용.
            val user_pref = this.getPreferences(Context.MODE_PRIVATE) // Context.MODE_PRIVATE 대신 0을 써도됨
            val user_editor = user_pref.edit()

            var number = binding.phone_number.text.toString()
            var pass = binding.password.text.toString()

            //            if(number.isNullOrBlank() && pass.isNullOrBlank()) { // 연락처, 비번 모두 빈칸
            //                var dialog = AlertDialog.Builder(this@LoginActivity)
            //                dialog.setTitle("로그인 에러")
            //                dialog.setMessage("모두 입력해 주세요")
            //                dialog.show()
            //            } else if(number.isNullOrBlank()) { // 연락처만 빈칸
            //                var dialog = AlertDialog.Builder(this@LoginActivity)
            //                dialog.setTitle("로그인 에러")
            //                dialog.setMessage("휴대전화를 입력해 주세요")
            //                dialog.show()
            //            } else if(pass.isNullOrBlank()) { // 비밀번호만 빈칸
            //                var dialog = AlertDialog.Builder(this@LoginActivity)
            //                dialog.setTitle("로그인 에러")
            //                dialog.setMessage("비밀번호를 입력해 주세요")
            //                dialog.show()
            //            }


            if(number.isNullOrBlank() || pass.isNullOrBlank()) {
                var dialog = AlertDialog.Builder(this@LoginActivity)
                dialog.setTitle("로그인 에러")
                dialog.setMessage("모두 입력해 주세요")
                dialog.show()
            } else {
                val data = LoginDto(number, pass)
                api.postLogin(data).enqueue(object: Callback<LoginDto> {
                    override fun onResponse(call: Call<LoginDto>, response: Response<LoginDto>) {
                        if(response.isSuccessful) { // 성공적으로 받아왔을 때
                            if(response.body()!!.success) {
                                Log.d("log", response.toString())
                                Log.d("log body", response.body().toString())

                                user_editor.putString("phone_number", number)
                                user_editor.putString("password", pass)
                                user_editor.commit()

                                Toast.makeText(this@LoginActivity, "로그인 완료", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                            else {
                                var dialog = AlertDialog.Builder(this@LoginActivity)
                                dialog.setTitle("로그인 에러")
                                dialog.setMessage("휴대전화, 비밀번호를 확인해 주세요.")
                                dialog.show()
                            }

                            if (!response.body().toString().isEmpty())
                                binding.text.setText(response.body().toString())
                        }

                    }

                    override fun onFailure(call: Call<LoginDto>, t: Throwable) {
                        Log.d("log", t.message.toString())
                        Log.d("log", "fail")

                    }
                })
            }

            startActivity(intent)
        }

        btn_register.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)

            startActivity(intent)
        }

    }



}
