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
        @SerializedName("address") val address: String
)

data class PNumCkDto(
        @SerializedName("phone_number") val phone_number: String
)