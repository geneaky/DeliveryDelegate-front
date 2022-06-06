package com.dongyang.daltokki.daldaepyo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.retrofit.SearchResponseDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import com.naver.maps.geometry.Tm128
import kotlinx.android.synthetic.main.activity_search_board.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchBoardActivity:AppCompatActivity() {

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

                    val xmap = response?.body()?.result?.get(0)?.mapx?.toDouble()!!
                    val ymap = response?.body()?.result?.get(0)?.mapy?.toDouble()!!

                    val tm128 = Tm128(xmap, ymap) // 좌표는 Double 형태로 넣어주어야 함
                    val latLng = tm128.toLatLng() // LatLng{latitude = , lngitude= } 형태

                    val lat = latLng.latitude // 위도만 빼주기
                    val lng = latLng.longitude // 경도만 빼주기

                    Log.d("검색", "${result}")
                    Log.d("log body", response.body().toString())
                    Log.d("mapx", xmap.toString())
                    Log.d("mapy", ymap.toString())
                    Log.d("map", "latLng" + latLng)
                    Log.d("lat", " " + lat)
                    Log.d("lat", " " + lng)
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