package com.dongyang.daltokki.daldaepyo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.databinding.ActivityMainBinding
import com.dongyang.daltokki.daldaepyo.databinding.ActivityRegisterBinding
import com.dongyang.daltokki.daldaepyo.retrofit.PNumCkDto
import com.dongyang.daltokki.daldaepyo.retrofit.RegisterDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.view.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    val api = UserAPI.create()
    var validate = 0 // 0이면 전화번호 중복확인 안함, 1이면 중복확인 함
    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        setContentView(binding.root)

//        // 전화번호 중복확인
//        binding.btnC.setOnClickListener {
//            var number = binding.edtPnumber.text.toString()
//            if(validate == 1) { // 중복확인 검증 완료
//                return@setOnClickListener
//            }
//            if(number.isNullOrBlank()) {
//                var dialog = AlertDialog.Builder(this@RegisterActivity)
//                dialog.setTitle("회원가입 에러")
//                dialog.setMessage("전화번호를 입력하세요.").setPositiveButton("확인", null)
//                dialog.show()
//                return@setOnClickListener
//            }
//            var data = PNumCkDto(number)
//            api.postPNumCk(data).enqueue(object : Callback<PNumCkDto> {
//                override fun onResponse(call: Call<PNumCkDto>, response: Response<PNumCkDto>) {
//                    var dialog = AlertDialog.Builder(this@RegisterActivity)
//                    dialog.setTitle("전화번호 중복확인 완료")
//                    dialog.setMessage("사용 가능한 아이디입니다.").setPositiveButton("확인", null)
//                    dialog.show()
//                    edtPnumber.setEnabled(false)
//                    validate = 1
//                    btnC.setBackgroundColor(Color.LTGRAY)
//
//                }
//
//                override fun onFailure(call: Call<PNumCkDto>, t: Throwable) {
//                    var dialog = AlertDialog.Builder(this@RegisterActivity)
//                    Log.d("회원가입", "${t.localizedMessage}")
//                    dialog.setTitle("전화번호 중복확인 에러")
//                    dialog.setMessage("이미 존재하는 전화번호입니다.").setPositiveButton("확인", null)
//                    dialog.show()
//                    validate = 0
//                }
//            })
//
//        }
//

        binding.btnRegister.setOnClickListener {

            var nickname = binding.edtNickname.text.toString()
            var number = binding.edtPnumber.text.toString()
            var pass1 = binding.edtpwd.text.toString()
            var pass2 = binding.edtpwdCheck.text.toString()




//
//            // 아이디 중복확인 체크
//            if(validate == 0) {
//                var dialog = AlertDialog.Builder(this@RegisterActivity)
//                dialog.setTitle("회원가입 에러")
//                dialog.setMessage("중복된 전화번호가 있는지 확인하세요.").setPositiveButton("확인", null)
//                dialog.show()
//                return@setOnClickListener
//            }
//
            // 한 칸이라도 입력하지 않았을 경우
            if(nickname.isBlank() || number.isBlank() || pass1.isBlank() ||
                    pass2.isBlank()) {
                var dialog = AlertDialog.Builder(this@RegisterActivity)
                dialog.setTitle("회원가입 에러")
                dialog.setMessage("모두 입력해 주세요").setPositiveButton("확인", null)
                dialog.show()
                return@setOnClickListener
            }


            val data = RegisterDto(nickname, number, pass1)
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
                        dialog.setMessage("휴대전화, 비밀번호를 확인해 주세요.").setPositiveButton("확인", null)
                        dialog.show()
                    }
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
