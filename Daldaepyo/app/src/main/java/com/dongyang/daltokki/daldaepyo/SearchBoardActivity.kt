package com.dongyang.daltokki.daldaepyo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.retofit.BoardItem
import com.dongyang.daltokki.daldaepyo.retrofit.SearchResponseDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import com.naver.maps.geometry.Tm128
import kotlinx.android.synthetic.main.activity_search_board.*
import kotlinx.android.synthetic.main.item_search_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 리스트뷰: https://philosopher-chan.tistory.com/1009

class SearchBoardActivity :AppCompatActivity() {


    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        setContentView(R.layout.activity_search_board)

    }

}