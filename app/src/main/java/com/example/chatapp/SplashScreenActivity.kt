package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.chatapp.baseData.BaseActivity
import com.example.chatapp.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed(Runnable {
           if (isLoggedIn()){
               val intent=Intent(this,ChatActivity::class.java)
               startActivity(intent)
               finish()
           }else{
               val intent=Intent(this,LoginActivity::class.java)
               startActivity(intent)
               finish()
           }
        },2000)
    }
}