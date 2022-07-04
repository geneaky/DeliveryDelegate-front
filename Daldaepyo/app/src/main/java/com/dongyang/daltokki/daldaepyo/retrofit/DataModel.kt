package com.dongyang.daltokki.daldaepyo.retrofit

import com.dongyang.daltokki.daldaepyo.retofit.LocationInfoResponseDto
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

data class StoreRegisterDto ( // 음식점 등록
        @SerializedName("store_name") val store_name : String,
        @SerializedName("store_posx") val store_posx : String,
        @SerializedName("store_posy") val store_posy : String,
        @SerializedName("store_address") val store_address : String
)

data class StoreRegisterResponseDto ( // 음식점 등록 완료 후 응답
        @SerializedName("store_id") val store_id: String
)

data class StoreFindResponseDto ( // 데이터베이스에서 음식점 찾기
        @SerializedName("store_id") val store_id : String,
        @SerializedName("message") val message : String
)

data class ImageResponseDto ( // 영수증 인증(OCR)
        @SerializedName("message") val message : String
)

data class ReviewDto(
        @SerializedName("review_id") val review_id: Int,
        @SerializedName("user_id") val user_id : Int,
        @SerializedName("store_id") val store_id : Int,
        @SerializedName("content") val content : String
)

data class GameDto(
        @SerializedName("game_type") val game_type: String,
        @SerializedName("game_name") val game_name: String,
        @SerializedName("population") val population: Int,
        @SerializedName("landmark_posx") val landmark_posx: String,
        @SerializedName("landmark_posy") val landmark_posy: String
)
data class GameResponseDto(
        @SerializedName("name") val name: String,
        @SerializedName("message") val message: String
)

