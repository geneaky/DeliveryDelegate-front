package com.dongyang.daltokki.daldaepyo.Review

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dongyang.daltokki.daldaepyo.R
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import kotlinx.android.synthetic.main.activity_review_detail.*

class ReviewDetailActivity : AppCompatActivity() {

    val api = UserAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_detail)

        val revPref = getSharedPreferences("review", 0)

        val store_name = revPref.getString("rStore_name","")
        val user_name = revPref.getString("rUser_name", "")
        val content = revPref.getString("rContent", "")
        val image_path = revPref.getString("rImg_path", "")
        val thumb_up = revPref.getInt("rThumb_up", 0).toString()

        tv_review_store.setText(store_name)
        tv_review_writer.setText(user_name)
        tv_review_content.setText(content)
        tv_thumbup_count.setText(thumb_up)
        Glide.with(this).load("http://146.56.132.245:8080/"+image_path).into(findViewById(R.id.img_review))

    }
}