package com.example.findmeapp.view.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.findmeapp.R
import com.example.findmeapp.databinding.ActivityMainBinding
import com.example.findmeapp.model.Repository
import com.example.findmeapp.view.main.fragment.chat.ChatFragment
import com.example.findmeapp.view.main.fragment.home.HomeFragment
import com.example.findmeapp.view.main.fragment.profile.ProfileFragment
import com.example.findmeapp.viewModel.MainViewModel
import com.example.findmeapp.viewModel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var repository: Repository
    private val fragmentHome = HomeFragment()
    private val fragmentChat = ChatFragment()
    private val fragmentProfile = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this , R.layout.activity_main)
        repository = Repository(FirebaseAuth.getInstance())
        val viewModel: MainViewModel by viewModels {
            ViewModelFactory(repository)
        }
        binding.mainActivityScreen = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        initSubView()
        addNavigationListener()
    }

    // Add addNavigationListener And Badge for bottomNavigationView
    private fun addNavigationListener() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.pagHome -> {
//                    var badge = binding.bottomNavigationView.getOrCreateBadge(R.id.pagHome)
//                    badge.isVisible = true
//                    badge.number = 99
                    replaceFragment(fragmentHome)
                    true
                }
                R.id.pagProfile -> {
                    replaceFragment(fragmentProfile)
                    true
                }
                R.id.pagMessage -> {
                    replaceFragment(fragmentChat)
                    true
                }
                else -> false
            }
        }
    }

    private fun initSubView() {
        addFragment(fragmentHome)
    }

    private fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainer,fragment)
        transaction.commit()
    }


    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer,fragment)
        transaction.commit()
    }

    private fun removeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.remove(fragment)
        transaction.commit()
    }

}