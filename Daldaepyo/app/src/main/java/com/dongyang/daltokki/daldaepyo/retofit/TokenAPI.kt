package com.dongyang.daltokki.daldaepyo.retofit

import android.app.Application
import android.content.SharedPreferences
import com.dongyang.daltokki.daldaepyo.retrofit.LatlngDto
import com.dongyang.daltokki.daldaepyo.retrofit.SearchResponseDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException

interface TokenAPI {

    @POST("/users/town")
//    @Headers("accept: application/json",
//        "content-type: application/json")
    fun postLatlng(@Body jsonparams: LatlngDto): Call<LatlngDto> // 동네 설정 좌표값

    @GET("/map/search/place")
//    @Headers("accept: application/json",
//            "content-type: application/json")
    fun getSearch(
            @Query("name") name : String ): Call<SearchResponseDto> // 음식점 검색


    @Multipart
    @POST("")
    fun postImage(@Part image: MultipartBody.Part): Call<String>


    companion object {
        private const val base_url = "http://146.56.132.245:8080/"

        fun createWithToken(): TokenAPI {
            val gson: Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                    .baseUrl(TokenAPI.base_url)
                    .client(provideOkHttpClient(AppInterceptor()))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(TokenAPI::class.java)
        }

        private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient = OkHttpClient.Builder().run {
            addInterceptor(interceptor)
            build()
        }

        class AppInterceptor : Application(), Interceptor {

//            lateinit var pref: SharedPreferences
            var tok: String = ""
            override fun onCreate() {
                var pref = getSharedPreferences("pref", 0)
                tok = pref.getString("token", "").toString()
                super.onCreate()
            }

            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response = with(chain) {

                val newRequest = request().newBuilder()
                        .addHeader("token", tok)
                        .build()
                return proceed(newRequest)
            }


        }

    }



}