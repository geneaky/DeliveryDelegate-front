package com.dongyang.daltokki.daldaepyo.Review.Store

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.R
import com.dongyang.daltokki.daldaepyo.databinding.ActivityLoginBinding
import com.dongyang.daltokki.daldaepyo.databinding.ActivityWriteReviewBinding
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import com.dongyang.daltokki.daldaepyo.retrofit.WriteReviewDto
import kotlinx.android.synthetic.main.activity_write_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WriteReviewActivity : AppCompatActivity() {

    val TAG = "리뷰쓰기@@@@"
    val binding by lazy { ActivityWriteReviewBinding.inflate(layoutInflater)}
    val api = UserAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        binding.btnSendReview.setOnClickListener {
            val pref = getSharedPreferences("pref", 0)
            val tok = pref.getString("token", "").toString()

            val storePref = getSharedPreferences("store", 0)
            val storeid = storePref.getString("storeid", "")!!.toInt()

            var review = binding.edtReview.text.toString()

            if(review.isBlank()){
                var dialog = AlertDialog.Builder(this@WriteReviewActivity, R.style.MyDialogTheme)
                dialog.setMessage("리뷰를 써주세요").setPositiveButton("확인", null)
                dialog.show()
                return@setOnClickListener
            }

            val data = WriteReviewDto(storeid,review)
            api.postWriteReview(tok,data).enqueue(object:Callback<WriteReviewDto>{
                override fun onResponse(
                    call: Call<WriteReviewDto>,
                    response: Response<WriteReviewDto>
                ) {
                    Log.d("log", response.toString())
                    Log.d("log body", response.body().toString())


                }

                override fun onFailure(call: Call<WriteReviewDto>, t: Throwable) {
                    Log.d("실패", t.message.toString())
                }
            })


        }



    }



}