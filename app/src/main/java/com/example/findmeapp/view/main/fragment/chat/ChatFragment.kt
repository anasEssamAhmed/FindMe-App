package com.example.findmeapp.view.main.fragment.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.findmeapp.databinding.FragmentChatBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val database = Firebase.database
    val reference = database.getReference()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentChatBinding.inflate(inflater,container,false)
//        binding.fabNotificationChat.setOnClickListener {
//            startActivity(Intent(context,NotificationActivity::class.java))
//        }
        return binding.root
    }

}