package com.example.findmeapp.view.main.fragment.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.findmeapp.R
import com.example.findmeapp.databinding.FragmentHomeBinding
import com.example.findmeapp.view.main.AddPostActivity
import com.example.findmeapp.view.main.NotificationActivity

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.fabNotificationHome.setOnClickListener {
            startActivity(Intent(context, NotificationActivity::class.java))
        }

        binding.fabAddPost.setOnClickListener {
            startActivity(Intent(context, AddPostActivity::class.java))
        }

        return binding.root
    }



}