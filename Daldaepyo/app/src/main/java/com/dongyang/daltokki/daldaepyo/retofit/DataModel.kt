package com.dongyang.daltokki.daldaepyo.retrofit

import com.dongyang.daltokki.daldaepyo.retofit.LocationInfoResponseDto
import com.dongyang.daltokki.daldaepyo.retofit.LoginInfoResponseDto
import com.google.gson.annotations.SerializedName

data class LoginDto( // 로그인
        @SerializedName("phone_number") val phone_number: String,
        @SerializedName("password") val password: String
)

data class RegisterDto( // 회원가입
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
)

data class PNumCkResponseDto( // 전화번호 중복확인 응답
        @SerializedName("message") val message: String
)

data class SearchResponseDto ( // 장소 검색
      @SerializedName("result") val result :  List<LocationInfoResponseDto>
)

data class ImageResponseDto (
        @SerializedName("fileInfo") val fileInfo : ImageInfoResponseDto
)
data class ImageInfoResponseDto (
        @SerializedName("fieldname") val fieldname : String,
        @SerializedName("originalname") val originalname : String
)

data class ReviewDto(
        @SerializedName("review_id") val review_id: Int,
        @SerializedName("user_id") val user_id : Int,
        @SerializedName("store_id") val store_id : Int,
        @SerializedName("content") val content : String
)
