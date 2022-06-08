package com.dongyang.daltokki.daldaepyo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.retrofit.StoreRegisterDto
import com.dongyang.daltokki.daldaepyo.retrofit.StoreRegisterResponseDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import kotlinx.android.synthetic.main.activity_store_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchReviewActivity : AppCompatActivity(), OnMapReadyCallback {

    val api = UserAPI.create()

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        setContentView(R.layout.activity_store_detail)

        val storePref = getSharedPreferences("store", 0) // 음식점 정보

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.store_map_view) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.store_map_view, it).commit()
            }
        mapFragment.getMapAsync(this)

        val store_name = storePref.getString("title", "").toString()
        val store_address = storePref.getString("address", "").toString()
        val store_posy = storePref.getString("lat", "").toString()
        val store_posx = storePref.getString("lng", "").toString()

        tv_store.setText(store_name) // 가게 이름 보여주기
        tv_store_address.setText(store_address) // 가게 주소 보여주기

        btn_store_regist.setOnClickListener {

            val pref = getSharedPreferences("pref", 0)
            val tok = pref.getString("token", "").toString()
            val data = StoreRegisterDto(store_name, store_posx, store_posy, store_address)

            api.postStore(tok, data).enqueue(object : Callback<StoreRegisterResponseDto> {
                override fun onResponse(call: Call<StoreRegisterResponseDto>, response: Response<StoreRegisterResponseDto>) {
                    Log.d("log", response.toString())
                    Log.d("log body", response.body().toString())

                    Toast.makeText(this@SearchReviewActivity, "음식점 등록이 완료되었습니다", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SearchReviewActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }

                override fun onFailure(call: Call<StoreRegisterResponseDto>, t: Throwable) {
                    Log.d("log", t.message.toString())
                    Log.d("log", "fail")

                    // Response로 받아오는 게 없어서 오류가 뜸
//                    Toast.makeText(this@SearchReviewActivity, "동네 설정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this@SearchReviewActivity, OcrActivity::class.java)
//                    startActivity(intent)
//                    finish()
                }
            })
        }

        btn_go_ocr.setOnClickListener {
            var intent = Intent(this@SearchReviewActivity, OcrActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    override fun onMapReady(naverMap: NaverMap) {
//        현재 카메라보다 좀 더 줌이 필요할 때 사용하기
//        this@LocationActivity.naverMap = naverMap
//        var camPos = CameraPosition(LatLng(latitude, longitude), 9.0)
//        naverMap.cameraPosition = camPos
        // 카메라 이동
        val storePref = getSharedPreferences("store", 0) // 음식점 정보
//            gpsTracker = GpsTracker(this@LocationActivity)
        val latitude : Double = storePref.getString("lat", "")!!.toDouble()
        val longitude : Double  = storePref.getString("lng", "")!!.toDouble()
        // (토스트 메시지는 테스트 끝나면 지우기)
//            Toast.makeText(this@LocationActivity, "현재위치 \n위도 $latitude\n경도 $longitude", Toast.LENGTH_LONG).show()
        Log.d("@@@현재위치: ", "위도 $latitude, 경도 $longitude")
        // 카메라 이동
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(latitude, longitude))
        naverMap.moveCamera(cameraUpdate)
        // 마커찍기
        val marker = Marker()
        marker.position = LatLng(latitude, longitude)
        marker.map = naverMap

    }
}