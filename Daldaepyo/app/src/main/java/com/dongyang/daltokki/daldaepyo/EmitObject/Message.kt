package com.dongyang.daltokki.daldaepyo.Game.EmitObject

import com.fasterxml.jackson.annotation.JsonAutoDetect

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class Message{
    lateinit var room_name : String
}