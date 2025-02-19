package com.example.findmeapp.view.main.fragment.profile

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.findmeapp.R
import com.example.findmeapp.databinding.ActivityEditInfoBinding
import com.example.findmeapp.model.Repository
import com.example.findmeapp.viewModel.MainViewModel
import com.example.findmeapp.viewModel.ViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class EditInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditInfoBinding
    private lateinit var repository: Repository
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_info)

        repository = Repository(FirebaseAuth.getInstance())
        val viewModel: MainViewModel by viewModels {
            ViewModelFactory(repository)
        }

        auth = Firebase.auth
        binding.editeInfoActivity = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.getInformation()


        binding.btnUpdateData.setOnClickListener {

            viewModel.updateDataUserInfo(
                auth.currentUser!!.uid,
                binding.tiedName.text.toString(),
                binding.tiedEmail.text.toString(),
                binding.tiedAddress.text.toString()
            )

            viewModel.statusMessage.observe(this) { message ->
                if (message != null) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }

            Log.d("TAGMo7ista", "EditeInfoActivity: true")

        }


    }
}