package com.dongyang.daltokki.daldaepyo.retrofit

import com.dongyang.daltokki.daldaepyo.retofit.LoginInfoResponseDto
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
    fun postLogin(@Body jsonparams: LoginDto): Call<LoginInfoResponseDto> // 로그인

<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
    @POST("/users/register") // 이게 맞을까..?
=======
    @POST("/users/register/existed")
>>>>>>> Stashed changes
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postPNumCk(@Body jsonparams: PNumCkDto): Call<PNumCkResponseDto> // 전화번호 중복확인

    @POST("/users/town")
    fun postLatlng(@Header("token") token: String,
            @Body jsonparams: LatlngDto): Call<LatlngDto> // 동네 설정 좌표값

    @GET("/map/search/place")
    fun getSearch(@Header("token") token: String,
            @Query("name") name : String ): Call<SearchResponseDto> // 음식점 검색

<<<<<<< Updated upstream
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
=======
    @Multipart
    @POST("/review/reciept")
    fun postImage(@Header("token") token: String,
                  @Part file: MultipartBody.Part): Call<ImageResponseDto>


>>>>>>> Stashed changes

    @GET("/store/:id/reviews")
    fun Review () : Call<ReviewDto>

>>>>>>> Stashed changes
    companion object {
        private const val base_url = "http://10.0.2.2:8080"

        fun create(): UserAPI {
            val gson: Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(UserAPI::class.java)
        }

    }


}