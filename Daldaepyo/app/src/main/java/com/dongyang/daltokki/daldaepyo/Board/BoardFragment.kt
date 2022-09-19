package com.dongyang.daltokki.daldaepyo.Board

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.daltokki.daldaepyo.Game.AttendOrderActivity
import com.dongyang.daltokki.daldaepyo.Game.CreateGame.CreateGameActivity
import com.dongyang.daltokki.daldaepyo.R
import com.dongyang.daltokki.daldaepyo.retrofit.FindGameResponseDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.fragment_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 리사이클러뷰: https://machine-woong.tistory.com/164
// 클릭 이벤트: https://machine-woong.tistory.com/186
// sharedPreferences: https://niqrid2020.pe.kr/sharedpreferences-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0kotlinandroid/
// https://weidianhuang.medium.com/android-fragment-not-attached-to-a-context-24d00fac4f3d

class BoardFragment : Fragment() {

    val api = UserAPI.create()

    val Board_Adapter: ArrayList<BoardItem> = ArrayList()
    lateinit var recyclerView1 : RecyclerView


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_board, container, false)

        val preferences = this.requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val tok = preferences.getString("token", "").toString()

        api.getFindGame(tok).enqueue(object : Callback<FindGameResponseDto> {
            override fun onResponse(call: Call<FindGameResponseDto>, response: Response<FindGameResponseDto>) {

                val result_size = response.body()?.games?.size
                val result = response.body().toString()
                Log.d("LOGGGG", result)
                val code = response.code()

                if (code == 200) {

                    for (i in 0 until result_size!!) {
                        val title = response?.body()?.games?.get(i)?.game_name.toString()
                        val population = response?.body()?.games?.get(i)?.population!!
                        val landmark = response?.body()?.games?.get(i)?.landmark_name.toString()
                        val write = response?.body()?.games?.get(i)?.game_main_text.toString()
                        val date = response?.body()?.games?.get(i)?.createdAt.toString()
                        val room_name = response?.body()?.games?.get(i)?.socket_room_name.toString()
                        val game_id = response?.body()?.games?.get(i)?.game_id!!

                        Board_Adapter.add(BoardItem(title, population, landmark, write, date))

                        checkIfFragmentAttached {
                            recyclerView1 = rootView.findViewById(R.id.recyclerView!!) as RecyclerView
                            recyclerView1.layoutManager = LinearLayoutManager(requireContext())
                            recyclerView1.adapter = BoardAdapter(requireContext(), Board_Adapter)

                            recyclerViewClickEvent(requireContext(), room_name, population, game_id)
                        }
                    }

                } else {
                    Log.d("ERRRRR", "검색에러")
                }
            }

            override fun onFailure(call: Call<FindGameResponseDto>, t: Throwable) {
                Log.e("게임검색", "${t.localizedMessage}")
            }
        })
        return rootView
    }


    fun recyclerViewClickEvent(context: Context, room: String,  population: Int, game_id: Int) {
        recyclerView1.addOnItemTouchListener(content_RecyclerView_ClickEvent(context, recyclerView1, object : content_RecyclerView_ClickEvent.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Toast.makeText(context, "주문한 가게를 검색하세요", Toast.LENGTH_SHORT).show()
                var sharedPreferences = SharedPreferenceT(requireContext())
                sharedPreferences.setString("room_name", room)
                sharedPreferences.setInt("population", population)
                sharedPreferences.setInt("game_id", game_id)

                val i = Intent(this@BoardFragment.getActivity(), AttendOrderActivity::class.java)
                startActivity(i)

            }

            override fun onItemLongClick(view: View, position: Int) {

            }
        }))
    }

    fun checkIfFragmentAttached(operation: Context.() -> Unit) {
        if(isAdded && context != null) {
            operation(requireContext())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_board_write.setOnClickListener {
            try {
                // TODO Auto-generated method stub
                val i = Intent(this@BoardFragment.getActivity(), CreateGameActivity::class.java)
                startActivity(i)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
