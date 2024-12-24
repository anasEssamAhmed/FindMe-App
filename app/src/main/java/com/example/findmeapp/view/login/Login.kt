package com.example.findmeapp.view.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.findmeapp.R
import com.example.findmeapp.databinding.ActivityLoginBinding
import com.example.findmeapp.view.passwordRecovery.PasswordRecovery

class Login : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_login)
        binding.lifecycleOwner = this
    }

    fun toPasswordRecoveryScreen(){
        startActivity(Intent(this , PasswordRecovery :: class.java))
    }
}