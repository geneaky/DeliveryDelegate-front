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
    fun postRegister(@Body jsonparams: RegisterDto): Call<RegisterDto>

    @POST("/users/login")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postLogin(@Body jsonparams: LoginDto): Call<LoginDto>

    @POST("/users/register") // 이게 맞을까..?
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postPNumCk(@Body jsonparams: PNumCkDto): Call<PNumCkDto>

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
    }



}