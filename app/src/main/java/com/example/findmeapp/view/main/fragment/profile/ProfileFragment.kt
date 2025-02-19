package com.example.findmeapp.view.main.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.findmeapp.databinding.FragmentProfileBinding
import com.example.findmeapp.model.Repository
import com.example.findmeapp.viewModel.MainViewModel
import com.example.findmeapp.viewModel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var repository: Repository
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        repository = Repository(FirebaseAuth.getInstance())
        val viewModel: MainViewModel by viewModels {
            ViewModelFactory(repository)
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.fragmentHome = this

        viewModel.getInformation()




        binding.btnEditInfo.setOnClickListener {
            startActivity(Intent(context, EditInfoActivity::class.java))
        }
        return binding.root
    }

}