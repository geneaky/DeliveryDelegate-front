package com.dongyang.daltokki.daldaepyo.Review.Store

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.dongyang.daltokki.daldaepyo.PermissionActivity
import com.dongyang.daltokki.daldaepyo.R
import com.dongyang.daltokki.daldaepyo.ReviewFragment
import com.dongyang.daltokki.daldaepyo.User.UserFragment
import com.dongyang.daltokki.daldaepyo.databinding.ActivityWriteReviewBinding
import com.dongyang.daltokki.daldaepyo.retrofit.ReviewResponseDto
import com.dongyang.daltokki.daldaepyo.retrofit.UserAPI
import com.dongyang.daltokki.daldaepyo.retrofit.WriteReviewDto
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
import java.io.File
import java.text.SimpleDateFormat
import java.util.Objects.isNull

class WriteReviewActivity : PermissionActivity() {

    val TAG = "리뷰로그@@@@@"
    val api = UserAPI.create()

    var image =""
    val PERM_STORAGE = 9
    val PERM_CAMERA = 10

    val REQ_GALLERY = 12

    var img = false




    val binding by lazy { ActivityWriteReviewBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        requirePermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PERM_STORAGE)


        binding.btnSendReview.setOnClickListener {

            val storePref = getSharedPreferences("store", 0)
            val store_id = storePref.getString("storeid", "")!!.toInt()

            if(img==false){

                val pref = getSharedPreferences("pref", 0)
                val tok = pref.getString("token", "").toString()

                var body = binding.edtReview.text.toString()

                val data = WriteReviewDto(store_id, body)

                if(body.isBlank()){
                    var dialog = AlertDialog.Builder(this@WriteReviewActivity, R.style.MyDialogTheme)
                    dialog.setMessage("리뷰를 써주세요").setPositiveButton("확인", null)
                    dialog.show()
                    return@setOnClickListener
                }

                api.postWriteReviewNo(tok,data).enqueue(object:Callback<ReviewResponseDto> {
                    override fun onResponse(
                        call: Call<ReviewResponseDto>,
                        response: Response<ReviewResponseDto>
                    ) {
                        val code = response.code()

                        
                        Log.d("log", response.toString())
                        Log.d("log body", response.body().toString())

                        if(code == 200) {
                            val message = response.body()?.message.toString()

                            finish()
                        }

                        if(code == 500){
                            val message = response.errorBody()?.string()!!
                            if(message.contains("bad")){
                                var dialog = AlertDialog.Builder(this@WriteReviewActivity)
                                dialog.setTitle("오류")
                                dialog.setMessage("욕하지 마세요").setPositiveButton("확인", null)
                                dialog.show()
                                return
                            }

                            if(message.contains("too")){

                                var dialog = AlertDialog.Builder(this@WriteReviewActivity)
                                dialog.setTitle("오류")
                                dialog.setMessage("10글자 이상 입력해주세요").setPositiveButton("확인", null)
                                dialog.show()
                                return
                            }

                        }
                        

//            val reviewBody = WriteReviewDto(review)

                    }
                    override fun onFailure(call: Call<ReviewResponseDto>, t: Throwable) {
                        Log.d("실패", t.message.toString())
                    }
                })
//
            } else{

                val pref = getSharedPreferences("pref", 0)
                val tok = pref.getString("token", "").toString()

                var body = binding.edtReview.text.toString()

                val file = File(image)
                val requestFile = RequestBody.create(MediaType.parse("image/png"), file) // Mime 타입
                val data = MultipartBody.Part.createFormData("file", file.name, requestFile)


                if(body.isBlank()){
                    var dialog = AlertDialog.Builder(this@WriteReviewActivity, R.style.MyDialogTheme)
                    dialog.setMessage("리뷰를 써주세요").setPositiveButton("확인", null)
                    dialog.show()
                    return@setOnClickListener
                }

                api.postWriteReview(tok,store_id, body, data).enqueue(object:Callback<ReviewResponseDto> {
                    override fun onResponse(
                            call: Call<ReviewResponseDto>,
                            response: Response<ReviewResponseDto>
                    ) {
                        val code = response.code()

                        Log.d("log", response.toString())
                        Log.d("log body", response.body().toString())

                        if(code == 200){
                            finish()

                        }

                        if(code == 500){
                            val message = response.errorBody()?.string()!!
                            if(message.contains("bad")){

                                var dialog = AlertDialog.Builder(this@WriteReviewActivity)
                                dialog.setTitle("오류")
                                dialog.setMessage("욕하지 마세요").setPositiveButton("확인", null)
                                dialog.show()
                                return
                            }

                            if(message.contains("too")){

                                var dialog = AlertDialog.Builder(this@WriteReviewActivity)
                                dialog.setTitle("오류")
                                dialog.setMessage("욕하지 마세요").setPositiveButton("확인", null)
                                dialog.show()
                                return
                            }

                        }



//            val reviewBody = WriteReviewDto(review)

                    }
                    override fun onFailure(call: Call<ReviewResponseDto>, t: Throwable) {
                        Log.d("실패", t.message.toString())
                    }
                })

            }

        }
    }



    fun initViews() {
        // 5. 갤러리 버튼이 클릭 되면 갤러리를 연다
        binding.btnLoadImage.setOnClickListener {
            openGallery()
            img = true
        }
    }

    var realUri: Uri? = null
    // 3. 카메라에 찍은 사진을 저장하기 위한 Uri를 넘겨준다.

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

    // 절대경로 변환
    fun absolutelyPath(path: Uri): String {

        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = contentResolver.query(path, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }

    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager){
        var ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }


}