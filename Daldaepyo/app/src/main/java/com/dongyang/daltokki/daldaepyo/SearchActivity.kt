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

            val pref = getSharedPreferences("pref", 0)
            val tok = pref.getString("token", "").toString()
            var name = edt_search.text.toString()

            api.getSearch(tok, name).enqueue(object: Callback<SearchResponseDto> {
                override fun onResponse(call: Call<SearchResponseDto>,
                    response: Response<SearchResponseDto>) {
                    val result = response.body()

                    var xmap = response?.body()?.result?.get(0)?.mapx
                    var ymap = response?.body()?.result?.get(0)?.mapy

                    Log.d("검색", "${result}")
                    Log.d("log body", response.body().toString())
                    Log.d("mapx", xmap.toString())
                    Log.d("mapy", ymap.toString())
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