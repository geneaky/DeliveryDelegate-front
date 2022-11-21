package com.dongyang.daltokki.daldaepyo.User

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.daltokki.daldaepyo.R
import com.dongyang.daltokki.daldaepyo.ReviewAdapter
import com.dongyang.daltokki.daldaepyo.retrofit.ReviewCountDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import com.dongyang.daltokki.daldaepyo.retrofit.UserReviewDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserReviewResponseDto
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.item_user_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserReviewActivity : AppCompatActivity() {

    val api = UserAPI.create()

    lateinit var mAdapter : UserReviewAdapter
    lateinit var rv : RecyclerView

    var datas = mutableListOf<UserReviewDto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_review)


        rv = findViewById(R.id.rv_user_review)
        mAdapter = UserReviewAdapter(this)
        rv.adapter = mAdapter
        rv.layoutManager = LinearLayoutManager(this)

//        img_review_delete.setOnClickListener {
//            mAdapter.delete(mAdapter.getPosition())
//        }

    }

    override fun onResume() {
        super.onResume()

        val pref = getSharedPreferences("pref", 0)
        val tok = pref.getString("token", "").toString()

        api.getUserReview(tok).enqueue(object : Callback<UserReviewResponseDto> {
            override fun onResponse(
                call: Call<UserReviewResponseDto>,
                response: Response<UserReviewResponseDto>
            ) {
                val result = response.body()?.message
                val code = response.code()

                if (code == 200) {
                    mAdapter.submitList(result!!)
                } else {
                    Log.d("error", "에러")
                }
            }

            override fun onFailure(call: Call<UserReviewResponseDto>, t: Throwable) {
                Log.e("리뷰", "${t.localizedMessage}")
            }

        })
    }
}