package com.dongyang.daltokki.daldaepyo.Review.Store

import android.provider.MediaStore
import android.text.TextUtils.isEmpty
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Objects.isNull

class WriteReviewActivity : PermissionActivity() {

    val TAG = "리뷰로그@@@@@"
    val binding by lazy { ActivityWriteReviewBinding.inflate(layoutInflater)}
    val api = UserAPI.create()
    lateinit var image : String

    val PERM_STORAGE = 10
    val REQ_GALLERY = 11

    val TAG = "리뷰쓰기@@@@"
    val binding by lazy { ActivityWriteReviewBinding.inflate(layoutInflater)}

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

//            val reviewBody = WriteReviewDto(review)

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
