package com.dongyang.daltokki.daldaepyo.Game.CreateGame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import kotlinx.android.synthetic.main.activity_landmark.*
import kotlinx.android.synthetic.main.activity_order.*

class OrderDetailActivity_Master : AppCompatActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_view2) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fm.beginTransaction().add(R.id.map_view2, it).commit()
                }
        mapFragment.getMapAsync(this)


        btn_order_detail.setOnClickListener {

            Toast.makeText(this@OrderDetailActivity_Master, "주문 상세 정보를 입력하세요.", Toast.LENGTH_LONG).show()
            val intent = Intent(this@OrderDetailActivity_Master, OrderDetailWriteActivity_Master::class.java)
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
        // 좌표 수집 및 현재위치 출력
        val Orderpref = getSharedPreferences("Orderpref", 0)
        val latitude : Double = Orderpref.getString("lat", "")!!.toDouble()
        val longitude : Double  = Orderpref.getString("lng", "")!!.toDouble()
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