package com.dongyang.daltokki.daldaepyo.Game

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dongyang.daltokki.daldaepyo.MainActivity
import com.dongyang.daltokki.daldaepyo.R
import kotlinx.android.synthetic.main.activity_game_start.*

class GameStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_start)

        Glide.with(this).load(R.raw.random_dice).into(imageView)
        loadSplashScreen()
    }

    private fun loadSplashScreen(){
        Handler().postDelayed({
            // You can declare your desire activity here to open after finishing splash screen. Like MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}