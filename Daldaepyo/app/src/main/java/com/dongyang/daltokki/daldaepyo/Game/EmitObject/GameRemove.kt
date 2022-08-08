package com.dongyang.daltokki.daldaepyo.Game.EmitObject

import com.fasterxml.jackson.annotation.JsonAutoDetect
import kotlin.properties.Delegates

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class GameRemove {
    lateinit var room_name : String
    var ranking by Delegates.notNull<Int>()
}