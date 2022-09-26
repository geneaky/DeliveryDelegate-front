package com.dongyang.daltokki.daldaepyo.Game.EmitObject

import com.fasterxml.jackson.annotation.JsonAutoDetect

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class LandmarkResult {
    lateinit var store_name : String
    lateinit var mapx : String
    lateinit var mapy : String
    lateinit var detail : String
}