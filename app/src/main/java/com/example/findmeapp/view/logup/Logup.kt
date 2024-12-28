package com.example.findmeapp.view.logup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.findmeapp.R
import com.example.findmeapp.databinding.ActivityLogupBinding
import com.example.findmeapp.model.Repository
import com.example.findmeapp.view.login.Login
import com.example.findmeapp.viewModel.MainViewModel
import com.example.findmeapp.viewModel.ViewModelFactory
import com.google.firebase.auth.*


class Logup : AppCompatActivity() {
    private lateinit var binding: ActivityLogupBinding
    private lateinit var repository: Repository
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_logup)
        repository = Repository(FirebaseAuth.getInstance())
        val viewModel: MainViewModel by viewModels {
            ViewModelFactory(repository)
        }
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.statusMessage.observe(this) { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.navigateToLogin.observe(this){
            if (it) {
                startActivity(Intent(this@Logup , Login::class.java))
            }
        }
        binding.ID.setOnClickListener {
            openGallery()
            if (selectedImageUri != null){
                Log.d("aaa" , "this is image url : $selectedImageUri")
                viewModel.imageUri.postValue(selectedImageUri)
            }
        }
    }

    // هنا يوجد مشكلة حيث لا يقوم بالتقاط الصورة وتخزينها في المتغير حيث من المفترض ان يقوم بتخزين الصورة ومن ثم يقوم برفع الصورة على ال firebase
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).setType("image/*")
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            Log.d("aaa" , selectedImageUri.toString())
        }
    }
}
