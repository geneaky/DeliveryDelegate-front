package com.dongyang.daltokki.daldaepyo.Board

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceT(context: Context) {
    private val defaultString = "0"

    private val instances: SharedPreferences by lazy {
        context.getSharedPreferences("Gamepref", Context.MODE_PRIVATE)
    }
    // sharedPreferences를 셋팅하는 함수.
    public fun setString(key: String, value: String) {
        instances.edit().putString(key, value).apply()
    }
    // sharedPreferences를 얻어오는 함수.
    public fun getString(key: String) : String? {
        return instances.getString(key, defaultString)
    }
    // sharedPreferences를 지우는 함수.
    public fun clearString(){
        instances.edit().clear().apply()
    }
}