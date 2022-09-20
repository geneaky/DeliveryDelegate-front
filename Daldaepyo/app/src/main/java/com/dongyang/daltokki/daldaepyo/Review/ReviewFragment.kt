package com.dongyang.daltokki.daldaepyo


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.daltokki.daldaepyo.Review.ReviewItem
import com.dongyang.daltokki.daldaepyo.Review.Store.WriteReviewActivity
import com.dongyang.daltokki.daldaepyo.retrofit.ReviewCountDto
import com.dongyang.daltokki.daldaepyo.retrofit.ThumbUpDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.fragment_review.*
import kotlinx.android.synthetic.main.item_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReviewFragment : Fragment() {

    val api = UserAPI.create()

    lateinit var adapter: ReviewAdapter

    lateinit var recyclerView1: RecyclerView

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

        adapter = ReviewAdapter(requireContext())

        recyclerView1 = rootView.findViewById(R.id.rv_review!!) as RecyclerView
        recyclerView1.layoutManager = LinearLayoutManager(requireContext())
        recyclerView1.adapter = adapter

        api.getReview(tok).enqueue(object : Callback<ReviewCountDto> {
            override fun onResponse(
                call: Call<ReviewCountDto>,
                response: Response<ReviewCountDto>
            ) {
                val result = response.body()?.message
                val code = response.code()

                if (code == 200) {
                    adapter.submitList(result!!)

                } else {
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

    override fun onResume() {
        super.onResume()

        recyclerView1.layoutManager = LinearLayoutManager(requireContext())
        adapter = ReviewAdapter(requireContext())
        recyclerView1.adapter = adapter

    }

    fun refresh(fragment: Fragment, fragmentManager: FragmentManager) {
        var ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }


}