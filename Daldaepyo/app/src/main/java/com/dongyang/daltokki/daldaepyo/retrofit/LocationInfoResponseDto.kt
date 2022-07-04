package com.dongyang.daltokki.daldaepyo.retofit

import com.google.gson.annotations.SerializedName

data class LocationInfoResponseDto (
    @SerializedName("title") var title: String,
    @SerializedName("link") val link: String,
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String,
    @SerializedName("telephone") val telephone: String,
    @SerializedName("address") val address: String,
    @SerializedName("roadAddress") val roadAddress: String,
    @SerializedName("mapx") val mapx: String,
    @SerializedName("mapy") val mapy: String
)