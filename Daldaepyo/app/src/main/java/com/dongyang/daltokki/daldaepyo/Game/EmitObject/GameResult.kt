package com.dongyang.daltokki.daldaepyo.Game.EmitObject

import com.fasterxml.jackson.annotation.JsonAutoDetect
import kotlin.properties.Delegates

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class GameResult {
    lateinit var token : String
    var game_id by Delegates.notNull<Int>()
}