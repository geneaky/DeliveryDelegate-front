package com.dongyang.daltokki.daldaepyo.Game.EmitObject

import com.fasterxml.jackson.annotation.JsonAutoDetect
import kotlin.properties.Delegates

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class DelegatorReRanking {
    lateinit var token : String
    var game_id by Delegates.notNull<Int>()
    lateinit var room_name : String
    lateinit var nickname : String
    var ranking by Delegates.notNull<Int>()
}