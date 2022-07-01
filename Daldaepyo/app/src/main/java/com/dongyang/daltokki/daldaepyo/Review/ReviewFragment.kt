package com.dongyang.daltokki.daldaepyo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.daltokki.daldaepyo.databinding.ActivityLoginBinding
import com.dongyang.daltokki.daldaepyo.databinding.FragmentReviewBinding
import com.dongyang.daltokki.daldaepyo.retrofit.ReviewDto
import kotlinx.android.synthetic.main.fragment_review.*
import retrofit2.Call
import java.lang.System.load

class ReviewFragment : Fragment() {



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_review, container, false)


        //load()


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_searchReview.setOnClickListener {
            try {
                // TODO Auto-generated method stub
                val i = Intent(this@ReviewFragment.getActivity(), SearchReviewActivity::class.java)
                startActivity(i)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}