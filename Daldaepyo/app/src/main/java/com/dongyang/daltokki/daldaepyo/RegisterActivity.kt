package com.dongyang.daltokki.daldaepyo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.databinding.ActivityMainBinding
import com.dongyang.daltokki.daldaepyo.retrofit.LoginDto
import com.dongyang.daltokki.daldaepyo.retrofit.RegisterDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import kotlinx.android.synthetic.main.activity_register.view.*
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
        // 3. 빈 칸이 없도록
        // 4. 비밀번호 재확인? 이런 거 안해도 되낭

        // 아이디 중복확인


        // 비밀번호 재확인





        binding.btn_register.setOnClickListener {

            var nickname = binding.edtNickname.text.toString()
            var number = binding.edtPnumber.text.toString()
            var pass1 = binding.edtpwd.text.toString()
            var pass2 = binding.edtpwdCheck.text.toString()
            var address = binding.edtAddress.text.toString()

            if(nickname.isNullOrBlank() || number.isNullOrBlank() || pass1.isNullOrBlank() ||
                    pass2.isNullOrBlank() || address.isNullOrBlank()) {
                var dialog = AlertDialog.Builder(this@RegisterActivity)
                dialog.setTitle("회원가입 에러")
                dialog.setMessage("모두 입력해 주세요")
                dialog.show()
            }

            val data = RegisterDto(binding.edtNickname.text.toString(),
                                 binding.edtPnumber.text.toString(),
                                 binding.edtpwd.text.toString(),
                    binding.edtAddress.text.toString())
            api.postRegister(data).enqueue(object : Callback<RegisterDto> {
                override fun onResponse(call: Call<RegisterDto>, response: Response<RegisterDto>) {
                    Log.d("log", response.toString())
                    Log.d("log body", response.body().toString())


                    if(pass1.equals(pass2)) { // 회원가입 성공(비밀번호가 동일함)
                        Toast.makeText(this@RegisterActivity, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()  // 회원가입등록 창 닫음
                    } else { // 비밀번호가 동일하지 않음
                        var dialog = AlertDialog.Builder(this@RegisterActivity)
                        dialog.setTitle("회원가입 에러")
                        dialog.setMessage("휴대전화, 비밀번호를 확인해 주세요.")
                        dialog.show()
                    }

                    if (!response.body().toString().isEmpty())
                        binding.text.setText(response.body().toString())
                }

                override fun onFailure(call: Call<RegisterDto>, t: Throwable) {
                    Log.d("log", t.message.toString())
                    Log.d("log", "fail")
                    Toast.makeText(this@RegisterActivity, "회원가입에 실패했습니다", Toast.LENGTH_SHORT).show()

                }
            })
        }


    }
}
