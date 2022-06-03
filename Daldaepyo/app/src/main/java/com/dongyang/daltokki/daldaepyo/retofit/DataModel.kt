package com.dongyang.daltokki.daldaepyo.retrofit

import com.google.gson.annotations.SerializedName

data class LoginDto(
        @SerializedName("phone_number") val phone_number: String,
        @SerializedName("password") val password: String
)

data class RegisterDto(
        @SerializedName("nickname") val nickname: String,
        @SerializedName("phone_number") val phone_number: String,
        @SerializedName("password") val password: String,
        @SerializedName("self_posx") val self_posx: String,
        @SerializedName("self_posy") val self_posy: String
)

data class PNumCkDto(
        @SerializedName("phone_number") val phone_number: String
)

data class ReviewDto(
        @SerializedName("review_id") val review_id: Int,
        @SerializedName("user_id") val user_id : Int,
        @SerializedName("store_id") val store_id : Int,
        @SerializedName("content") val content : String

)
