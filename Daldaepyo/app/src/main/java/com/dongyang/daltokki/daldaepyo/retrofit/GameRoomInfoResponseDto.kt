package com.dongyang.daltokki.daldaepyo.retrofit

import com.google.gson.annotations.SerializedName

data class GameRoomInfoResponseDto( // 게임방 상세 정보
    @SerializedName("game_id") val game_id: Int,
    @SerializedName("game_type") val game_type: String,
    @SerializedName("game_name") val game_name: String,
    @SerializedName("game_main_text") val game_main_text: String,
    @SerializedName("population") val population: Int,
    @SerializedName("landmark_name") val landmark_name: String,
    @SerializedName("landmark_posx") val landmark_posx: String,
    @SerializedName("landmark_posy") val landmark_posy: String,
    @SerializedName("socket_room_name") val socket_room_name: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)
