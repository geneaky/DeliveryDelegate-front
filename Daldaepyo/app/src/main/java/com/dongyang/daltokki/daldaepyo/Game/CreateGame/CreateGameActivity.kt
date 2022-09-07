package com.dongyang.daltokki.daldaepyo.Game.CreateGame

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.GameActivity
import com.dongyang.daltokki.daldaepyo.R
import kotlinx.android.synthetic.main.activity_create_game.*


class CreateGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_game)

        btn_landmark.setOnClickListener {
            var Gametype = edt_game_type.text.toString()
            var Gamename = edt_game_name.text.toString()
            var Population = edt_population.text.toString().toInt()
            var Gametext = edt_game_main_text.text.toString() // server: game_main_text

            val Gamepref = getSharedPreferences("Gamepref", 0)
            val edit = Gamepref.edit()
            edit.apply()
            edit.putString("Gametype", Gametype)
            edit.putString("Gamename", Gamename)
            edit.putInt("Population", Population)
            edit.putString("Gametext", Gametext)
            edit.commit()

            val intent = Intent(this@CreateGameActivity, LandmarkActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}