package com.example.findmeapp.view.passwordRecovery

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.findmeapp.R
import com.example.findmeapp.databinding.ActivityPasswordRecoveryBinding
import com.example.findmeapp.model.Repository
import com.example.findmeapp.viewModel.MainViewModel
import com.example.findmeapp.viewModel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class PasswordRecovery : AppCompatActivity() {
    private lateinit var binding : ActivityPasswordRecoveryBinding
    private lateinit var repository: Repository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_password_recovery)
        repository = Repository(FirebaseAuth.getInstance())
        val viewModel: MainViewModel by viewModels {
            ViewModelFactory(repository)
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.statusMessage.observe(this){ message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}