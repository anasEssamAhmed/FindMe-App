package com.example.findmeapp.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.findmeapp.R
import com.example.findmeapp.databinding.ActivityLoginBinding
import com.example.findmeapp.model.Repository
import com.example.findmeapp.view.main.MainActivity
import com.example.findmeapp.view.logup.Logup
import com.example.findmeapp.view.passwordRecovery.PasswordRecovery
import com.example.findmeapp.viewModel.MainViewModel
import com.example.findmeapp.viewModel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var repository: Repository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_login)
        repository = Repository(FirebaseAuth.getInstance())
        val viewModel: MainViewModel by viewModels {
            ViewModelFactory(repository)
        }
        binding.mainScreen = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigateToMain.observe(this){
            if (it) {
                startActivity(Intent(this@Login , MainActivity::class.java))
            }
        }

        viewModel.statusMessage.observe(this) { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun toPasswordRecoveryScreen(){
        startActivity(Intent(this , PasswordRecovery :: class.java))
    }

    fun toCreateAccountScreen(){
        startActivity(Intent(this , Logup :: class.java))
    }
}