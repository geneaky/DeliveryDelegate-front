package com.dongyang.daltokki.daldaepyo

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

abstract class PermissionActivity : AppCompatActivity() {

    abstract fun permissionGranted(requestCode: Int)
    abstract fun permissionDenied(requestCode: Int)

    // 권한 검사
    fun requirePermissions (permission:Array<String>, requestCode: Int) {
        // API 버전이 마시멜로 미만이면 권하처리가 필요없음
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionGranted(requestCode)
        } else {
            ActivityCompat.requestPermissions(this, permission, requestCode)
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.all {it == PackageManager.PERMISSION_GRANTED}) {
            permissionGranted(requestCode)
        } else {
            permissionDenied(requestCode)
        }
    }
}