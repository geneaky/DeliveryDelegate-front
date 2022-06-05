package com.dongyang.daltokki.daldaepyo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.retrofit.SearchResponseDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import kotlinx.android.synthetic.main.activity_search_board.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity:AppCompatActivity() {

    val api = UserAPI.create()
    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        setContentView(R.layout.activity_search_board)

        btn_searchBoard1.setOnClickListener {

            var name = edt_search.text.toString()

            api.getSearch(name).enqueue(object: Callback<SearchResponseDto> {
                override fun onResponse(call: Call<SearchResponseDto>,
                    response: Response<SearchResponseDto>) {
                    val result = response.body()
                    Log.d("검색", "${result}")
                    Log.d("log body", response.body().toString())
//                    var intent = Intent(this@SearchBoardActivity, SearchReviewActivity::class.java)
//                    startActivity(intent)
//                    finish()
                }

                override fun onFailure(call: Call<SearchResponseDto>, t: Throwable) {
                    Log.e("검색", "${t.localizedMessage}")
                }
            })

        }
    }

}