package com.example.findmeapp.view.main.fragment.chat

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmeapp.R
import com.example.findmeapp.databinding.ActivityMessageBinding
import com.example.findmeapp.model.Repository
import com.example.findmeapp.viewModel.MainViewModel
import com.example.findmeapp.viewModel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding
    private var repository: Repository = Repository(FirebaseAuth.getInstance())
    private val TAG = "mo7Ismail"
    private val auth = FirebaseAuth.getInstance()
    val viewModel: MainViewModel by viewModels {
        ViewModelFactory(repository)
    }

    private lateinit var manager: LinearLayoutManager
    private lateinit var uid: String
    private lateinit var tokenId: String
//    private lateinit var adapter: FriendlyMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message)


        binding.messageActivity = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

//        viewModel.statusMessage.observe(this) { message ->
//            if (message != null) {
//                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//            }
//        }


    }
}