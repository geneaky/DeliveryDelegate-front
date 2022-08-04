package com.dongyang.daltokki.daldaepyo.Game.EmitObject

import com.fasterxml.jackson.annotation.JsonAutoDetect

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class QuitGame {
    lateinit var token : String
    lateinit var nickname : String
    lateinit var room_name : String
}