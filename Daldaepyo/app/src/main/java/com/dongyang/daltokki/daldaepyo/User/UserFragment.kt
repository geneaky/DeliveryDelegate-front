package com.dongyang.daltokki.daldaepyo.User

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.fragment.app.Fragment
import com.dongyang.daltokki.daldaepyo.LocationActivity
import com.dongyang.daltokki.daldaepyo.R
import kotlinx.android.synthetic.main.fragment_review.*
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)


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

    }
}