package com.dongyang.daltokki.daldaepyo.Game.Landmark

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.MainActivity
import com.dongyang.daltokki.daldaepyo.R
import com.dongyang.daltokki.daldaepyo.retrofit.GameDto
import com.dongyang.daltokki.daldaepyo.retrofit.GameResponseDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import kotlinx.android.synthetic.main.activity_landmark.*
import kotlinx.android.synthetic.main.activity_location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LandmarkDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmark)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_view2) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_view2, it).commit()
            }
        mapFragment.getMapAsync(this)


        btn_create_game.setOnClickListener {

            val pref = getSharedPreferences("pref", 0)
            val tok = pref.getString("token", "").toString()

            val Gamepref = getSharedPreferences("Gamepref", 0)
            var gametype = Gamepref.getString("Gametype", "").toString()
            var gamename = Gamepref.getString("Gamename", "").toString()
            var gametext = Gamepref.getString("Gametext", "").toString()
            var population = Gamepref.getString("Population", "").toString().toInt()
            var landmark_name = Gamepref.getString("title", "").toString()
            var landmark_posx = Gamepref.getString("lng", "").toString()
            var landmark_posy = Gamepref.getString("lat", "").toString()


            val data = GameDto(gametype, gamename, gametext, population, landmark_name, landmark_posx, landmark_posy) // x: 경도, y: 위도
            api.postCreateGame(tok, data).enqueue(object : Callback<GameResponseDto> {
                override fun onResponse(call: Call<GameResponseDto>, response: Response<GameResponseDto>) {
                    Log.d("log", response.toString())
                    Log.d("log body", response.body().toString())
                    val code = response.code()

                    if(code == 200) {
                        val room_name = response.body()?.name
                        val edit = Gamepref.edit() // 수정모드
                        edit.apply()
                        edit.putString("room_name", room_name)
                        edit.commit()

                        Toast.makeText(this@LandmarkDetailActivity, "게임방 생성이 완료되었습니다", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LandmarkDetailActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LandmarkDetailActivity, "게임방 생성에 실패했습니다. 관리자에게 문의해 주세요.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<GameResponseDto>, t: Throwable) {
                    Log.d("log", t.message.toString())
                    Log.d("log", "fail")


                }
            })
        }
    }
    override fun onMapReady(naverMap: NaverMap) {
//        현재 카메라보다 좀 더 줌이 필요할 때 사용하기
//        this@LocationActivity.naverMap = naverMap
//        var camPos = CameraPosition(LatLng(latitude, longitude), 9.0)
//        naverMap.cameraPosition = camPos
        // 카메라 이동
        // 좌표 수집 및 현재위치 출력
        val Gamepref = getSharedPreferences("Gamepref", 0)
        val latitude : Double = Gamepref.getString("lat", "")!!.toDouble()
        val longitude : Double  = Gamepref.getString("lng", "")!!.toDouble()
        // (토스트 메시지는 테스트 끝나면 지우기)
        Log.d("@@@현재위치: ", "위도 $latitude, 경도 $longitude")
        // 카메라 이동
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(latitude, longitude))
        naverMap.moveCamera(cameraUpdate)
        // 마커찍기
        val marker = Marker()
        marker.position = LatLng(latitude, longitude)
        marker.map = naverMap
        // 마커 이동

    }
}