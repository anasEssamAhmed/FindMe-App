package com.example.findmeapp.view.main.fragment.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.findmeapp.R
import com.example.findmeapp.databinding.FragmentHomeBinding
import com.example.findmeapp.databinding.FragmentProfileBinding
import com.example.findmeapp.view.login.Login


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentProfileBinding.inflate(inflater,container,false)

        binding.btnEditInfo.setOnClickListener {
            startActivity(Intent(context, EditInfoActivity::class.java))
        }
        return binding.root
    }

}