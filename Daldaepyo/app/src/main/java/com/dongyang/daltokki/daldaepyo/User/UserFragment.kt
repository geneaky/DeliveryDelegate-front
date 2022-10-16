package com.dongyang.daltokki.daldaepyo.User

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.dongyang.daltokki.daldaepyo.LocationActivity
import com.dongyang.daltokki.daldaepyo.R
import com.dongyang.daltokki.daldaepyo.ReviewFragment
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import com.dongyang.daltokki.daldaepyo.retrofit.UserDto
import kotlinx.android.synthetic.main.fragment_review.*
import kotlinx.android.synthetic.main.fragment_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFragment : Fragment() {


    val api = UserAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_user, container, false)

        return rootView

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tv_user_location.setOnClickListener {
            try {
                // TODO Auto-generated method stub
                val i = Intent(this@UserFragment.getActivity(), LocationActivity::class.java)
                startActivity(i)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        tv_user_modify.setOnClickListener{
            try {
                // TODO Auto-generated method stub
                val i = Intent(this@UserFragment.getActivity(), UserModifyActivity::class.java)
                startActivity(i)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        tv_user_notice.setOnClickListener{
            try {
                // TODO Auto-generated method stub
                val i = Intent(this@UserFragment.getActivity(), NoticeActivity::class.java)
                startActivity(i)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        tv_user_ask.setOnClickListener{
            try {
                // TODO Auto-generated method stub
                val i = Intent(this@UserFragment.getActivity(), AskActivity::class.java)
                startActivity(i)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        tv_user_review_count.setOnClickListener{
            try {
                // TODO Auto-generated method stub
                val i = Intent(this@UserFragment.getActivity(), AskActivity::class.java)
                startActivity(i)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    override fun onResume() {
        super.onResume()

        val preferences = this.requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val tok = preferences.getString("token", "").toString()

        api.getUser(tok).enqueue(object : Callback<UserDto>{
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                val code = response.code()
                Log.d("User", code.toString())

                if(response.isSuccessful){
                    Log.d("닉네임",response?.body()?.userInfo?.nickname.toString())

                    val nickname = response?.body()?.userInfo?.nickname.toString()
                    val coupon_count = response?.body()?.userInfo?.coupon_count.toString()
                    val count = response?.body()?.userInfo?.count.toString()

                    tv_user_name.setText(nickname)
                    tv_user_review_count.setText(count)
                    tv_review_coupon_count.setText(coupon_count)
                } else{
                    Toast.makeText(requireContext(), "사용자 정보 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }



            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                Log.e("사용자", "${t.localizedMessage}")
            }
        })

    }


}