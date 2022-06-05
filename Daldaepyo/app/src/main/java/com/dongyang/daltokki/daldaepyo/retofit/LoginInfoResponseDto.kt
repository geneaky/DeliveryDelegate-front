package com.dongyang.daltokki.daldaepyo.retofit

import com.google.gson.annotations.SerializedName

data class LoginInfoResponseDto(
        @SerializedName("user") val user: LoginUserDto,
        @SerializedName("token") val token: LoginTokenDto
)

data class LoginUserDto(
        @SerializedName("user_id") val user_id: String,
        @SerializedName("phone_number") val phone_number: String,
        @SerializedName("nickname") val nickname: String,
        @SerializedName("self_posx") val self_posx: String,
        @SerializedName("self_posy") val self_posy: String
)

data class LoginTokenDto(
        @SerializedName("token") val token: String,
        @SerializedName("refreshToken") val refreshToken: String
)