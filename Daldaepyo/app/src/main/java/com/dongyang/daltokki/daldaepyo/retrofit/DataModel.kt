package com.dongyang.daltokki.daldaepyo.retrofit

import android.provider.ContactsContract
import android.text.format.DateFormat
import com.dongyang.daltokki.daldaepyo.retofit.LocationInfoResponseDto
import com.google.gson.annotations.SerializedName
import org.w3c.dom.Text

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
        @SerializedName("content") val content: String,
        @SerializedName("thumb_up") val thumb_up : Int,
        @SerializedName("user_name") val user_name : String,
        @SerializedName("store_name") val store_name : String,
        @SerializedName("image_path") val image_path : String,
        @SerializedName("review_id") val review_id : Int
)

data class ReviewCountDto(
        @SerializedName("message") val message: List<ReviewDto>
)

data class GameDto( // 게임 생성
        @SerializedName("game_type") val game_type: String,
        @SerializedName("game_name") val game_name: String,
        @SerializedName("game_main_text") val game_main_text: String,
        @SerializedName("population") val population: Int,
        @SerializedName("landmark_name") val landmark_name: String,
        @SerializedName("landmark_posx") val landmark_posx: String,
        @SerializedName("landmark_posy") val landmark_posy: String,
        @SerializedName("order") val order: OrderDto
)
data class OrderDto( // 음식 주문 정보
        @SerializedName("store_name") val store_name: String,
        @SerializedName("mapx") val mapx: String,
        @SerializedName("mapy") val mapy: String,
        @SerializedName("detail") val detail: String
)
data class GameResponseDto( // 게임 생성 응답
        @SerializedName("name") val name: String,
        @SerializedName("message") val message: String,
        @SerializedName("game_id") val game_id: Int
)

data class FindGameResponseDto( // 게임방 검색
        @SerializedName("games") val games : List<GameRoomInfoResponseDto>
)

data class ReviewResponseDto(
        @SerializedName("message") val message : String
)


data class WriteReviewDto(
        @SerializedName ("store_id") var store_id: Int,
        @SerializedName("body") var body : String
)

data class ThumbUpDto(
        @SerializedName("review_id") var review_id : Int
)

data class UserDto(
        @SerializedName("userInfo")var userInfo : User
)

data class User(
        @SerializedName("user_id") var user_id : Int,
        @SerializedName("phone_number") val phone_number: String,
        @SerializedName("nickname") val nickname : String,
        @SerializedName("coupon_count") val coupon_count : Int,
        @SerializedName("count") val count : Int
)

