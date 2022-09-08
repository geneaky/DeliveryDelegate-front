package com.dongyang.daltokki.daldaepyo.Game

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dongyang.daltokki.daldaepyo.R
import kotlinx.android.synthetic.main.activity_game_random.*

class GameRandomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_random)

        Glide.with(this).load(R.raw.random_dice).into(imageView)
        loadSplashScreen()
    }

    private fun loadSplashScreen(){
        Handler().postDelayed({
            // You can declare your desire activity here to open after finishing splash screen. Like MainActivity
            val intent = Intent(this, GameResultActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}