package com.dongyang.daltokki.daldaepyo


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.retrofit.*
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

class StoreDetailActivity : AppCompatActivity(), OnMapReadyCallback {

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

        btn_go_ocr.setOnClickListener {


            val pref = getSharedPreferences("pref", 0)
            val tok = pref.getString("token", "").toString()
            val data = StoreRegisterDto(store_name, store_posx, store_posy, store_address)

            /*
                리뷰쓰기 버튼을 누르면
               1. store find로 있는지 확인
                 1-1. 있으면 OCR로 "영수증 리뷰가 필요합니다."
                 1-2. 없으면 store register로 등록
                      OCR로 "영수증 리뷰가 필요합니다."
             */

            api.postStoreFind(tok, store_name, store_posx, store_posy).enqueue(object : Callback<StoreFindResponseDto> {
                override fun onResponse(call: Call<StoreFindResponseDto>, response: Response<StoreFindResponseDto>) {
                    Log.d("log", response.toString())
                    Log.d("log body", response.code().toString())
                    val code = response.code()

                    if(code == 200) {
                        val store_id = response?.body()?.store_id.toString()
                        val edit = storePref.edit() // 수정모드
                        edit.apply()
                        edit.putString("store_id", store_id)
                        edit.commit()

                        Toast.makeText(this@StoreDetailActivity, "영수증 인증이 필요합니다.", Toast.LENGTH_SHORT).show()
                        var intent = Intent(this@StoreDetailActivity, OcrActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    if(code == 404) {
                        api.postStore(tok, data).enqueue(object : Callback<StoreRegisterResponseDto> {
                            override fun onResponse(call: Call<StoreRegisterResponseDto>, response: Response<StoreRegisterResponseDto>) {
                                Log.d("log", response.toString())
                                Log.d("log body", response.body().toString())
                                val code = response.code()
                                val store_id = response?.body()?.store_id.toString()

                                if(code == 200) {
                                    val edit = storePref.edit() // 수정모드
                                    edit.apply()
                                    edit.putString("store_id", store_id)
                                    edit.commit()

                                    Toast.makeText(this@StoreDetailActivity, "음식점 등록이 완료되었습니다", Toast.LENGTH_SHORT).show()
                                    Toast.makeText(this@StoreDetailActivity, "영수증 인증이 필요합니다", Toast.LENGTH_SHORT).show()
                                    var intent = Intent(this@StoreDetailActivity, OcrActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                if(code == 500) {
                                    Toast.makeText(this@StoreDetailActivity, "음식점 등록에 실패했습니다. 관리자에게 문의해 주세요.", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<StoreRegisterResponseDto>, t: Throwable) {
                                Log.d("log", t.message.toString())
                                Log.d("log", "fail")

                            }
                        })
                    }
                    if(code == 500) {
                        Toast.makeText(this@StoreDetailActivity, "음식점 찾기에 실패했습니다. 관리자에게 문의해 주세요.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<StoreFindResponseDto>, t: Throwable) {
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
        val storePref = getSharedPreferences("store", 0) // 음식점 정보
//            gpsTracker = GpsTracker(this@LocationActivity)
        val latitude : Double = storePref.getString("lat", "")!!.toDouble()
        val longitude : Double  = storePref.getString("lng", "")!!.toDouble()
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
