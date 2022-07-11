package com.dongyang.daltokki.daldaepyo

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    val api = UserAPI.create()

    var REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmark)

        if (checkLocationServicesStatus()) {
            checkRunTimePermission()
        } else {
            showDialogForLocationServiceSetting()
        }
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
            var landmark_posx = Gamepref.getString("lng", "").toString()
            var landmark_posy = Gamepref.getString("lat", "").toString()


            val data = GameDto(gametype, gamename, gametext, population, landmark_posx, landmark_posy) // x: 경도, y: 위도
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
                        val intent = Intent(this@LandmarkDetailActivity, CreateGameActivity::class.java)
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
    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    override fun onRequestPermissionsResult(permsRequestCode: Int,
                                            permissions: Array<String>,
                                            grandResults: IntArray) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults)
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.size == REQUIRED_PERMISSIONS.size) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            var check_result = true


            // 모든 퍼미션을 허용했는지 체크합니다.
            for (result in grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {

                //위치 값을 가져올 수 있음
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(this@LandmarkDetailActivity, "퍼미션이 거부되었습니다. 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this@LandmarkDetailActivity, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(this@LandmarkDetailActivity,
            Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this@LandmarkDetailActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@LandmarkDetailActivity, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(this@LandmarkDetailActivity, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this@LandmarkDetailActivity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this@LandmarkDetailActivity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private fun showDialogForLocationServiceSetting() {
        val builder = AlertDialog.Builder(this@LandmarkDetailActivity)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("""
    앱을 사용하기 위해서는 위치 서비스가 필요합니다.
    위치 설정을 수정하실래요?
    """.trimIndent())
        builder.setCancelable(true)
        builder.setPositiveButton("설정") { dialog, id ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
        }
        builder.setNegativeButton("취소") { dialog, id -> dialog.cancel() }
        builder.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음")
                        checkRunTimePermission()
                        return
                    }
                }
        }
    }

    fun checkLocationServicesStatus(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

    companion object {
        private const val GPS_ENABLE_REQUEST_CODE = 2001
        private const val PERMISSIONS_REQUEST_CODE = 100
    }
}