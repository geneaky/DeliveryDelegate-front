package com.dongyang.daltokki.daldaepyo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.daltokki.daldaepyo.Review.ReviewItem
import com.dongyang.daltokki.daldaepyo.Review.Store.WriteReviewActivity
import com.dongyang.daltokki.daldaepyo.databinding.ActivityLoginBinding
import com.dongyang.daltokki.daldaepyo.databinding.FragmentReviewBinding
import com.dongyang.daltokki.daldaepyo.retrofit.ReviewCountDto
import com.dongyang.daltokki.daldaepyo.retrofit.ReviewDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import kotlinx.android.synthetic.main.fragment_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.System.load

class ReviewFragment : Fragment() {


    val api = UserAPI.create()

    val Review_Adapter : ArrayList<ReviewItem> = ArrayList()
    lateinit var recyclerView1 : RecyclerView


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_review, container, false)

        val prefereces = this.requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val tok = prefereces.getString("token", "").toString()



        api.getReview(tok).enqueue(object : Callback<ReviewCountDto>{
            override fun onResponse(call: Call<ReviewCountDto>, response: Response<ReviewCountDto>) {
                val result_size = response.body()?.reviews?.size ?: 0
                val result = response.body().toString()
                Log.d("Log", result)
                val code = response.code()

                if (code == 200){
                    for ( i in 0 until result_size){
                        val store = response?.body()?.reviews?.get(i)?.store_name.toString()
                        val writer = response?.body()?.reviews?.get(i)?.user_name.toString()
                        val content = response?.body()?.reviews?.get(i)?.content.toString()

                        Review_Adapter.add(ReviewItem(store, writer, content))

                        recyclerView1 = rootView.findViewById(R.id.rv_review!!) as RecyclerView
                        recyclerView1.layoutManager = LinearLayoutManager(requireContext())
                        recyclerView1.adapter = ReviewAdapter(Review_Adapter, requireContext())
                    }
                }
                else {
                    Log.d("error", "에러")
                }
            }

            override fun onFailure(call: Call<ReviewCountDto>, t: Throwable) {
                Log.e("리뷰", "${t.localizedMessage}")
            }

        })




        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_searchReview.setOnClickListener {
            try {
                // TODO Auto-generated method stub
                val i = Intent(this@ReviewFragment.getActivity(), SearchStoreActivity::class.java)
                startActivity(i)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        btn_review_write.setOnClickListener {
            try {
                // TODO Auto-generated method stub
                val i = Intent(this@ReviewFragment.getActivity(), WriteReviewActivity::class.java)
                startActivity(i)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}