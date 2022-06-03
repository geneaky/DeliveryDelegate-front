package com.dongyang.daltokki.daldaepyo.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UserAPI {
    @POST("/users/register")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postRegister(@Body jsonparams: RegisterDto): Call<RegisterDto> // 회원가입

    @POST("/users/login")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postLogin(@Body jsonparams: LoginDto): Call<LoginDto> // 로그인

    @POST("/users/register/existed")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postPNumCk(@Body jsonparams: PNumCkDto): Call<PNumCkResponseDto> // 전화번호 중복확인

    @POST("/users/register")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postLatlng(@Body jsonparams: LatlngDto): Call<LatlngDto> // 동네 설정 좌표값

    @GET("/map/search/place")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun getSearch(
        @Query("name") name : String ): Call<SearchResponseDto> // 음식점 검색


    @GET("/store/:id/reviews")
    fun Review () : Call<ReviewDto>
    companion object {
        private const val base_url = "http://146.56.132.245:8080/"

        fun create(): UserAPI {
            val gson: Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(UserAPI::class.java)
        }

        private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient = OkHttpClient.Builder().run {
            addInterceptor(interceptor)
            build()
        }

        class AppInterceptor : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
                val newRequest = request().newBuilder()
                    .addHeader("token", "(header Value)")
                    .build()
                proceed(newRequest)
            }
        }
    }



}
