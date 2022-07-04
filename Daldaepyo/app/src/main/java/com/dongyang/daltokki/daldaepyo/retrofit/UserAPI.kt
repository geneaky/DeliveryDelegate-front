package com.dongyang.daltokki.daldaepyo.retrofit

import com.dongyang.daltokki.daldaepyo.retofit.LoginInfoResponseDto
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
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

    @POST("/users/register/existed")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postPNumCk(@Body jsonparams: PNumCkDto): Call<PNumCkResponseDto> // 전화번호 중복확인

    @POST("/users/town")
    fun postLatlng(@Header("token") token: String,
            @Body jsonparams: LatlngDto): Call<LatlngDto> // 동네 설정 좌표값

    @GET("/map/search/place")
    fun getSearch(@Header("token") token: String,
            @Query("name") name : String ): Call<SearchResponseDto> // 음식점 검색

    @POST("/store/register")
    fun postStore(@Header("token") token: String,
                  @Body jsonparams: StoreRegisterDto): Call<StoreRegisterResponseDto> // 음식점 등록

    @GET("/store/")
    fun postStoreFind(@Header("token") token: String,
                      @Query("store_name") store_name: String,
                      @Query("store_posx") store_posx: String,
                      @Query("store_posy") store_posy: String
    ): Call<StoreFindResponseDto> // 음식점 등록 전 찾기

    @Multipart
    @POST("/review/reciept")
    fun postImage(@Header("token") token: String,
                  @Part("store_id") store_id: Int,
                  @Part file: MultipartBody.Part): Call<ImageResponseDto> // 이미지 전송

    @GET("/store/:id/reviews")
    fun Review () : Call<ReviewDto>

    @POST("/game/register")
    fun postCreateGame(@Header("token") token: String,
                 @Body jsonparams: GameDto): Call<GameResponseDto> // 게임방 생성


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

    }



}