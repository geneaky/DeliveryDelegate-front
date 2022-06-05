package com.dongyang.daltokki.daldaepyo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
<<<<<<< Updated upstream
import kotlinx.android.synthetic.main.activity_login.*
=======
import com.dongyang.daltokki.daldaepyo.databinding.ActivityMainBinding
import com.dongyang.daltokki.daldaepyo.retofit.LoginInfoResponseDto
import com.dongyang.daltokki.daldaepyo.retrofit.LoginDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
>>>>>>> Stashed changes

class LoginActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    val api = UserAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setContentView(binding.root)
<<<<<<< Updated upstream
        loadSplashScreen()



        binding.btn_login.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)

            // 값을 저장할 땐 editor 사용. apply 적용하기..!!
            // 값을 가져올 땐 pref 사용.
            val user_pref = this.getPreferences(Context.MODE_PRIVATE) // Context.MODE_PRIVATE 대신 0을 써도됨
            val user_editor = user_pref.edit()

<<<<<<< Updated upstream
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
=======
            var number = binding.edt_id.text.toString()
            var pass = binding.edt_id.text.toString()
>>>>>>> Stashed changes


            if(number.isNullOrBlank() || pass.isNullOrBlank()) {
=======

        val pref = getSharedPreferences("pref", 0)
        val savedId = pref.getString("id", "").toString()
        Log.d(TAG, savedId)
        val tok = pref.getString("token", "")
        Log.d("getString@@@@@@@@@", "" + tok)

        binding.btnLogin.setOnClickListener {

            var id = binding.edtPnumber.text.toString()
            var pw = binding.edtPwd.text.toString()

            // 한 칸이라도 입력하지 않았을 경우
            if(id.isBlank() || pw.isBlank()) {
>>>>>>> Stashed changes
                var dialog = AlertDialog.Builder(this@LoginActivity)
                dialog.setTitle("로그인 에러")
                dialog.setMessage("모두 입력해 주세요").setPositiveButton("확인", null)
                dialog.show()
<<<<<<< Updated upstream
            } else {
                val data = LoginDto(number, pass)
                api.postLogin(data).enqueue(object: Callback<LoginDto> {
                    override fun onResponse(call: Call<LoginDto>, response: Response<LoginDto>) {
                        if(response.isSuccessful) { // 성공적으로 받아왔을 때
//                            if(response.body()!!.success) {
                                Log.d("log", response.toString())
                                Log.d("log body", response.body().toString())

                                user_editor.putString("phone_number", number)
                                user_editor.putString("password", pass)
                                user_editor.commit()

                                Toast.makeText(this@LoginActivity, "로그인 완료", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
//                            }



//                            if (!response.body().toString().isEmpty())
//                                binding.text.setText(response.body().toString())
                        }
                        else {
                            var dialog = AlertDialog.Builder(this@LoginActivity)
                            dialog.setTitle("로그인 에러")
                            dialog.setMessage("전화번호, 비밀번호를 확인해 주세요.").setPositiveButton("확인", null)
                            dialog.show()
                            return
                        }

                    }

                    override fun onFailure(call: Call<LoginDto>, t: Throwable) {
                        Log.d("log", t.message.toString())
                        Log.d("log", "fail")
                        return
                    }
                })
            }

            startActivity(intent)
        }

        binding.btn_register.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)

=======
                return@setOnClickListener
            }

            val data = LoginDto(id, pw)
            api.postLogin(data).enqueue(object:Callback<LoginInfoResponseDto>{
                override fun onResponse(call: Call<LoginInfoResponseDto>, response: Response<LoginInfoResponseDto>) {
                    val result = response.body()
                    Log.d("로그인", "${result}")
                    Log.d("log body", response.body().toString())

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

                override fun onFailure(call: Call<LoginInfoResponseDto>, t: Throwable) {
                    Log.e("로그인", "${t.localizedMessage}")
                }
            })
        }


        binding.btnRegister.setOnClickListener{
<<<<<<< Updated upstream
<<<<<<< Updated upstream
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
>>>>>>> Stashed changes
=======
            var intent = Intent(this@LoginActivity, LocationActivity::class.java)
>>>>>>> Stashed changes
=======
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
>>>>>>> Stashed changes
            startActivity(intent)
        }

    }



}
