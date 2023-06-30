package com.ltud.ltud_app.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.ltud.ltud_app.R
import com.ltud.ltud_app.onboard.OnboardActivity
import org.checkerframework.checker.units.qual.Length

class SplashActivity : AppCompatActivity() {
    private lateinit var onboardingScreen : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            onboardingScreen = getSharedPreferences("onboardingScreen", MODE_PRIVATE)

            val isFirstTime : Boolean = onboardingScreen.getBoolean("firstTime",true)
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()

            if (isFirstTime){
                val editor : SharedPreferences.Editor = onboardingScreen.edit()
                editor.putBoolean("firstTime", false)
                editor.commit()
                startActivity(Intent(this, OnboardActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

        }, 3000)
    }

}