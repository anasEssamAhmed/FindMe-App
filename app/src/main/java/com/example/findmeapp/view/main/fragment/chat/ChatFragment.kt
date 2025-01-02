package com.example.findmeapp.view.main.fragment.chat

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.findmeapp.R
import com.example.findmeapp.databinding.FragmentChatBinding
import com.example.findmeapp.databinding.FragmentHomeBinding
import com.example.findmeapp.view.main.NotificationActivity


class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatBinding.inflate(inflater,container,false)

//        binding.fabNotificationChat.setOnClickListener {
//            startActivity(Intent(context,NotificationActivity::class.java))
//        }
        return binding.root
    }

}