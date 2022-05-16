package com.dongyang.daltokki.daldaepyo.retrofit

import com.google.gson.annotations.SerializedName

data class LoginDto(
        @SerializedName("phone_number") val phone_number: String,
        @SerializedName("password") val password: String
)

data class RegisterDto(
    @SerializedName("phone_number") val phone_number: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String
)