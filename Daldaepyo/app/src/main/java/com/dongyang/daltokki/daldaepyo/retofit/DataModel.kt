package com.dongyang.daltokki.daldaepyo.retrofit

import com.google.gson.annotations.SerializedName

data class LoginDto(
        @SerializedName("phone_number") val phone_number: String,
        @SerializedName("password") val password: String
)

data class RegisterDto(
<<<<<<< Updated upstream
    @SerializedName("phone_number") val phone_number: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String
=======
        @SerializedName("nickname") val nickname: String,
        @SerializedName("phone_number") val phone_number: String,
        @SerializedName("password") val password: String,
        @SerializedName("self_posx") val self_posx: String,
        @SerializedName("self_posy") val self_posy: String
)

data class PNumCkDto(
        @SerializedName("phone_number") val phone_number: String
>>>>>>> Stashed changes
)