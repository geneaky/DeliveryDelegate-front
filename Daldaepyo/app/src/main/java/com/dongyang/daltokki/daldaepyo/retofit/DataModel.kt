package com.dongyang.daltokki.daldaepyo.retrofit

import com.google.gson.annotations.SerializedName

data class LoginDto( // 로그인
        @SerializedName("phone_number") val phone_number: String,
        @SerializedName("password") val password: String
)

<<<<<<< Updated upstream
data class RegisterDto(
<<<<<<< Updated upstream
    @SerializedName("phone_number") val phone_number: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String
=======
=======
data class RegisterDto( // 회원가입
>>>>>>> Stashed changes
        @SerializedName("nickname") val nickname: String,
        @SerializedName("phone_number") val phone_number: String,
        @SerializedName("password") val password: String
)

data class LatlngDto( // 동네설정 좌표값
        @SerializedName("self_posx") val self_posx: String,
        @SerializedName("self_posy") val self_posy: String
)

data class PNumCkDto( // 전화번호 중복확인
        @SerializedName("phone_number") val phone_number: String
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
)

data class PNumCkResponseDto( // 전화번호 중복확인 응답
        @SerializedName("message") val message: String
)

data class SearchResponseDto ( // 장소 검색
      @SerializedName("result") val result :  List<LocationInfoResponseDto>

>>>>>>> Stashed changes
)