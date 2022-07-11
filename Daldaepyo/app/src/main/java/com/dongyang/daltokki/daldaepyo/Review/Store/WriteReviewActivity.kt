package com.dongyang.daltokki.daldaepyo.Review.Store

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
<<<<<<< Updated upstream
import android.util.Log
=======
import android.provider.MediaStore
import android.text.TextUtils.isEmpty
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
>>>>>>> Stashed changes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dongyang.daltokki.daldaepyo.PermissionActivity
import com.dongyang.daltokki.daldaepyo.R
import com.dongyang.daltokki.daldaepyo.databinding.ActivityLoginBinding
import com.dongyang.daltokki.daldaepyo.databinding.ActivityWriteReviewBinding
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import com.dongyang.daltokki.daldaepyo.retrofit.WriteReviewDto
import kotlinx.android.synthetic.main.activity_write_review.*
<<<<<<< Updated upstream
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
=======
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Objects.isNull
>>>>>>> Stashed changes

class WriteReviewActivity : PermissionActivity() {

    val TAG = "리뷰로그@@@@@"
    val binding by lazy { ActivityWriteReviewBinding.inflate(layoutInflater)}
    val api = UserAPI.create()
    lateinit var image : String

    val PERM_STORAGE = 10
    val REQ_GALLERY = 11

    val TAG = "리뷰쓰기@@@@"
    val binding by lazy { ActivityWriteReviewBinding.inflate(layoutInflater)}
    val api = UserAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

<<<<<<< Updated upstream


        binding.btnSendReview.setOnClickListener {
            val pref = getSharedPreferences("pref", 0)
            val tok = pref.getString("token", "").toString()

            val storePref = getSharedPreferences("store", 0)
            val storeid = storePref.getString("storeid", "")!!.toInt()

            var review = binding.edtReview.text.toString()

=======
        binding.btnSendReview.setOnClickListener {
//            val pref = getSharedPreferences("pref", 0)
//            val tok = pref.getString("token", "").toString()

//            val storePref = getSharedPreferences("store", 0)
//            val storeid = storePref.getString("storeid", "")!!.toInt()

            val tok : String = "2"
            val storeid = 1

            var review = binding.edtReview.text.toString()

            val file = File(image)
            val requestFile = RequestBody.create(MediaType.parse("image/png"), file) // Mime 타입
            val data = MultipartBody.Part.createFormData("file", file.name, requestFile)

            requirePermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PERM_STORAGE)

>>>>>>> Stashed changes
            if(review.isBlank()){
                var dialog = AlertDialog.Builder(this@WriteReviewActivity, R.style.MyDialogTheme)
                dialog.setMessage("리뷰를 써주세요").setPositiveButton("확인", null)
                dialog.show()
                return@setOnClickListener
            }

<<<<<<< Updated upstream
            val data = WriteReviewDto(storeid,review)
            api.postWriteReview(tok,data).enqueue(object:Callback<WriteReviewDto>{
                override fun onResponse(
                    call: Call<WriteReviewDto>,
                    response: Response<WriteReviewDto>
                ) {
                    Log.d("log", response.toString())
                    Log.d("log body", response.body().toString())

=======
//            val reviewBody = WriteReviewDto(review)

            api.postWriteReview(tok,storeid,review, data).enqueue(object: Callback<WriteReviewDto> {
                override fun onResponse(
                        call: Call<WriteReviewDto>,
                        response: Response<WriteReviewDto>
                ) {
                    Log.d("log", response.body().toString())
                    Log.d("log body", response.body().toString())
                    Log.d("result", response.code().toString())
>>>>>>> Stashed changes

                }

                override fun onFailure(call: Call<WriteReviewDto>, t: Throwable) {
                    Log.d("실패", t.message.toString())
                }
            })
<<<<<<< Updated upstream


        }



    }



}
=======
        }

    }

    var realUri: Uri? = null

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQ_GALLERY)
    }

    fun initViews() {
        // 5. 갤러리 버튼이 클릭 되면 갤러리를 연다
        binding.btnLoadImage.setOnClickListener {
            openGallery()
        }
    }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK) {
            when(requestCode) {
                REQ_GALLERY -> {
                    data?.data?.let { uri ->
                        binding.imgReview.setImageURI(uri)
                        // 절대 경로
                        image = absolutelyPath(uri)
                        Log.d("!!!!절대경로!!!!", "Gallery " + image)

                    }
                }
            }
        }

    }

    override fun permissionGranted(requestCode: Int) {
        when(requestCode) {
            PERM_STORAGE -> initViews()
        }
    }

    override fun permissionDenied(requestCode: Int) {
        when(requestCode) {
            PERM_STORAGE -> {
                Toast.makeText(this, "저장소 권한을 승인해야 앱을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                finish()
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

>>>>>>> Stashed changes
