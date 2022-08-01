package com.dongyang.daltokki.daldaepyo.Game.EmitObject

import com.fasterxml.jackson.annotation.JsonAutoDetect
import kotlin.properties.Delegates

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class AttendGame {
    lateinit var token : String
    var game_id by Delegates.notNull<Int>()
    lateinit var room_name : String
    var size by Delegates.notNull<Int>()
    lateinit var order : AttendGame_Order
}