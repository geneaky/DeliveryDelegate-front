package com.dongyang.daltokki.daldaepyo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.dongyang.daltokki.daldaepyo.Review.ReviewItem
import com.dongyang.daltokki.daldaepyo.Review.Store.WriteReviewActivity
import com.dongyang.daltokki.daldaepyo.databinding.FragmentReviewBinding
import com.dongyang.daltokki.daldaepyo.retrofit.ReviewCountDto
import com.dongyang.daltokki.daldaepyo.retrofit.ReviewDto
import com.dongyang.daltokki.daldaepyo.retrofit.ThumbUpDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import com.google.gson.internal.LinkedTreeMap
import kotlinx.android.synthetic.main.fragment_review.*
import kotlinx.android.synthetic.main.item_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import java.lang.System.load
import java.lang.reflect.Member

class ReviewFragment : Fragment() {

    val api = UserAPI.create()

    val Review_Adapter : ArrayList<ReviewItem> = ArrayList()
    lateinit var recyclerView1 : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_review, container, false)


        val preferences = this.requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val tok = preferences.getString("token", "").toString()

        val reviewPref = this.requireActivity().getSharedPreferences("review", 0)
        val review = reviewPref.getInt("review",0)

        val data = ThumbUpDto(review)

        api.getReview(tok).enqueue(object : Callback<ReviewCountDto>{
            override fun onResponse(call: Call<ReviewCountDto>, response: Response<ReviewCountDto>) {
                val result_size = response.body()?.message?.size ?: 0
                val result = response.body().toString()
                Log.d("Log", result)
                val code = response.code()

                if (code == 200){
                    for ( i in 0 until result_size){
                        val store_name = response?.body()?.message?.get(i)?.store_name.toString()
                        val user_name = response?.body()?.message?.get(i)?.user_name.toString()
                        val content = response?.body()?.message?.get(i)?.content.toString()
                        val image_path = response?.body()?.message?.get(i)?.image_path.toString()
                        val thumb_up = response?.body()?.message?.get(i)?.thumb_up!!
                        val review_id = response?.body()?.message?.get(i)?.review_id!!.toInt()

                        val reviewPrefs : SharedPreferences = context!!.getSharedPreferences("review", Context.MODE_PRIVATE)
                        val revEditor : SharedPreferences.Editor = reviewPrefs.edit()

                        revEditor.putInt("review", review_id)
                        revEditor.putString("rStore_name", store_name)
                        revEditor.putString("rUser_name", user_name)
                        revEditor.putString("rContent", content)
                        revEditor.putString("rImg_path", image_path)
                        revEditor.putInt("rThumb_up",thumb_up)
                        revEditor.commit()




                        Review_Adapter.add(ReviewItem(store_name, user_name, content, image_path, thumb_up))

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
                activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.remove(this)
                        ?.commit()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    fun refresh(fragment:Fragment, fragmentManager: FragmentManager){
        var ft : FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }


}