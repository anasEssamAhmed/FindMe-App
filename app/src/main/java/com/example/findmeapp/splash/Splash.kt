package com.example.findmeapp.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.findmeapp.R
import com.example.findmeapp.login.Login

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}