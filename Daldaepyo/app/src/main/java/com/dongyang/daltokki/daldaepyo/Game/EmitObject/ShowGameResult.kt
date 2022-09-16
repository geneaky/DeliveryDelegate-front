package com.dongyang.daltokki.daldaepyo.Game.EmitObject

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ShowGameResult @JsonCreator constructor(
        @JsonProperty("store_name") val store_name: String,
        @JsonProperty("mapx") val mapx: String,
        @JsonProperty("mapy") val mapy: String,
        @JsonProperty("detail") val detail: String
)
