package com.dongyang.daltokki.daldaepyo.Review.Store

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.PermissionActivity
import com.dongyang.daltokki.daldaepyo.R
import com.dongyang.daltokki.daldaepyo.databinding.ActivityWriteReviewBinding
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import com.dongyang.daltokki.daldaepyo.retrofit.WriteReviewDto
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart

class WriteReviewActivity : AppCompatActivity() {
    val api = UserAPI.create()

    lateinit var image : String


    val binding by lazy {ActivityWriteReviewBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        binding.btnSendReview.setOnClickListener {
//            val pref = getSharedPreferences("pref", 0)
//            val tok = pref.getString("token", "").toString()

//            val storePref = getSharedPreferences("store", 0)
//            val storeid = storePref.getString("storeid", "")!!.toInt()

            val tok : String = "2"
            val storeid = 1

            var body = binding.edtReview.text.toString()
            var data = MultipartBody.Part.createFormData("file", null)

            val review = WriteReviewDto(body)

            if(body.isBlank()){
                var dialog = AlertDialog.Builder(this@WriteReviewActivity, R.style.MyDialogTheme)
                dialog.setMessage("리뷰를 써주세요").setPositiveButton("확인", null)
                dialog.show()
                return@setOnClickListener
            }

            api.postWriteReview(tok,storeid,review,data).enqueue(object: Callback<WriteReviewDto> {
                override fun onResponse(
                        call: Call<WriteReviewDto>,
                        response: Response<WriteReviewDto>
                ) {
                    Log.d("log", response.toString())
                    Log.d("log body", response.body().toString())
                    Log.d("result", response.code().toString())

                }
                override fun onFailure(call: Call<WriteReviewDto>, t: Throwable) {
                    Log.d("실패", t.message.toString())
                }
            })


        }
    }

}