package com.dongyang.daltokki.daldaepyo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.dongyang.daltokki.daldaepyo.Board.BoardFragment
import com.dongyang.daltokki.daldaepyo.Notification.NotifFragment
import com.dongyang.daltokki.daldaepyo.User.UserFragment
import com.dongyang.daltokki.daldaepyo.databinding.ActivityMainBinding

@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val fragmentBoard by lazy {BoardFragment()}
    private val fragmentReview by lazy {ReviewFragment()}
    private val fragmentUser by lazy {UserFragment()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(view)


        initNavigationBar()
    }


    private fun initNavigationBar(){
        binding.bottomNb.run{
            setOnItemSelectedListener{ item ->
                when(item.itemId){
                    R.id.menu_board ->
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.main_frame, fragmentBoard)
                            .commit()
                    R.id.menu_review ->
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.main_frame, fragmentReview)
                            .commit()
//                    R.id.menu_notification ->
//                        changeFragment(NotifFragment())
                    R.id.menu_user ->
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.main_frame, fragmentUser)
                            .commit()
                }
                true
            }
            selectedItemId = R.id.menu_board
        }
    }

    private fun changeFragment(targetFragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, targetFragment)
            .commitAllowingStateLoss()
    }


    companion object{
        private const val TAG = "MainActivity"
    }


}