package com.dongyang.daltokki.daldaepyo

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.dongyang.daltokki.daldaepyo.databinding.ActivityOcrBinding
import com.dongyang.daltokki.daldaepyo.retrofit.ImageResponseDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat

// 참고자료
// 절대 경로: https://machine-woong.tistory.com/174
// 이미지 전송(Multipart): https://gaybee.tistory.com/42
// MIME 타입: https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types
// 카메라/갤러리 이미지 가져오기: https://flow9.net/bbs/board.php?bo_table=thisisandroid&wr_id=117&device=pc

class OcrActivity: PermissionActivity() {
    val api = UserAPI.create()

    lateinit var image : String

    val PERM_STORAGE = 9
    val PERM_CAMERA = 10

    val REQ_CAMERA = 11
    val REQ_GALLERY = 12

    val binding by lazy { ActivityOcrBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        // 1. 공용저장소 권한이 있는지 확인
        requirePermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PERM_STORAGE)

        
        binding.btnOcr.setOnClickListener {
            val pref = getSharedPreferences("pref", 0)
            val tok = pref.getString("token", "").toString()

            Log.d("!!!!절대경로!!!!", "Camera/Gallery " + image)
            val file = File(image)
            val requestFile = RequestBody.create(MediaType.parse("image/png"), file) // Mime 타입
            val data = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val storePref = getSharedPreferences("store", 0)
<<<<<<< Updated upstream
            val store_id = storePref.getString("store_id", "").toString()

            val data = ImageDto(store_id, file)
            api.postImage(tok, data).enqueue(object : Callback<ImageResponseDto> {
                override fun onResponse(call: Call<ImageResponseDto>, response: Response<ImageResponseDto>) {
                    Log.d("log", response.toString())
                    Log.d("log body", response.body().toString())
                    val result = response.body()?.message.toString()

                    if (result == "Reciept Verified") {
                        Toast.makeText(this@OcrActivity, "영수증 인증을 완료했습니다.", Toast.LENGTH_SHORT).show()
                        // 리뷰 작성하는 페이지로 전환
                        val intent = Intent(this@OcrActivity, StoreDetailActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    if (result == "ImageResponseDto(message=Reciept recognition failure)") {
                        var dialog = AlertDialog.Builder(this@OcrActivity, R.style.MyDialogTheme)
                        dialog.setTitle("영수증 인증 에러")
                        dialog.setMessage("영수증을 확인해 주세요").setPositiveButton("확인", null)
                        dialog.show()
                        return
                    }

                }
=======
            val store_id = storePref.getString("store_id", "")?.toInt()

            if (store_id != null) {
                api.postImage(tok, store_id, data).enqueue(object : Callback<ImageResponseDto> {
                    override fun onResponse(call: Call<ImageResponseDto>, response: Response<ImageResponseDto>) {
                        Log.d("log", response.toString())
                        Log.d("log body", response.body()?.message.toString())
                        val result = response.body()?.message.toString()
                        Log.d("result", result)

                        // 우선 toast로 받아오기(추후 변경 예정)
                        Toast.makeText(this@OcrActivity, result, Toast.LENGTH_LONG).show()

//                        if (result == "Reciept Verified") {
//                            Toast.makeText(this@OcrActivity, "영수증 인증을 완료했습니다.", Toast.LENGTH_SHORT).show()
//                            // 리뷰 작성하는 페이지로 전환
//                            val intent = Intent(this@OcrActivity, StoreDetailActivity::class.java)
//                            startActivity(intent)
//                            finish()
//                        }
//                        if (result == "Reciept recognition failure") {
//                            var dialog = AlertDialog.Builder(this@OcrActivity)
//                            dialog.setTitle("영수증 인증 에러")
//                            dialog.setMessage("영수증을 확인해 주세요").setPositiveButton("확인", null)
//                            dialog.show()
//                            return
//                        }
                    }
>>>>>>> Stashed changes

                    override fun onFailure(call: Call<ImageResponseDto>, t: Throwable) {
                        // Response로 받아오는 게 없어서 오류가 뜸
                        Log.d("log", t.message.toString())
                        Log.d("log", "fail")
                        Log.e("error", "" + t)
                    }
                })
            }
        }

    }

    fun initViews() {
        // 2. 카메라 요청시 권한을 먼저 체크하고 승인되었으면 카메라를 연다.
        binding.btnCamera.setOnClickListener {
            requirePermissions(arrayOf(Manifest.permission.CAMERA), PERM_CAMERA)
        }
        // 5. 갤러리 버튼이 클릭 되면 갤러리를 연다
        binding.btnGallery.setOnClickListener {
            openGallery()
        }
    }

    // 원본 이미지의 주소를 저장할 변수
    var realUri: Uri? = null
    // 3. 카메라에 찍은 사진을 저장하기 위한 Uri를 넘겨준다.
    fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        createImageUri(newFileName(), "image/jpg")?.let { uri ->
            realUri = uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)
            startActivityForResult(intent, REQ_CAMERA)
        }
    }
    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQ_GALLERY)
    }
    // 원본 이미지를 저장할 Uri를 MediaStore(데이터베이스)에 생성하는 메서드
    fun createImageUri(filename:String, mimeType:String) : Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }
    // 파일 이름을 생성하는 메서드
    fun newFileName() : String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "${filename}.jpg"
    }
    // 원본 이미지를 불러오는 메서드
    fun loadBitmap(photoUri: Uri) : Bitmap? {
        try {
            return if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                val source = ImageDecoder.createSource(contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
            }
        } catch(e:Exception) {
            e.printStackTrace()
        }
        return null
    }


    override fun permissionGranted(requestCode: Int) {
        when(requestCode) {
            PERM_STORAGE -> initViews()
            PERM_CAMERA -> openCamera()
        }
    }

    override fun permissionDenied(requestCode: Int) {
        when(requestCode) {
            PERM_STORAGE -> {
                Toast.makeText(this, "저장소 권한을 승인해야 앱을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
            PERM_CAMERA -> Toast.makeText(this, "카메라 권한을 승인해야 카메라를 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    // 4. 카메라를 찍은 후에 호출된다. 6. 갤러리에서 선택 후 호출된다
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK) {
            when(requestCode) {
                REQ_CAMERA -> {
//                    val bitmap = data?.extras?.get("data") as Bitmap // 미리보기 이미지
//                    binding.imagePreview.setImageBitmap(bitmap)
                    realUri?.let { uri ->
                        val bitmap = loadBitmap(uri)
                        binding.imgPreview.setImageBitmap(bitmap)
                        realUri = null
                        // 절대 경로
                        image = absolutelyPath(uri)
                        Log.d("!!!!절대경로!!!!", "Camera " + image)
                    }
                }
                REQ_GALLERY -> {
                    data?.data?.let { uri ->
                        binding.imgPreview.setImageURI(uri)
                        // 절대 경로
                        image = absolutelyPath(uri)
                        Log.d("!!!!절대경로!!!!", "Gallery " + image)
                    }
                }
            }
        }

    }

    // 절대경로 변환
    fun absolutelyPath(path: Uri): String {

        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = contentResolver.query(path, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }
}