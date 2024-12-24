package com.example.findmeapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmeapp.model.Repository

class MainViewModel(private val repository: Repository = Repository()) : ViewModel() {
    private val _navigateToForgotPassword = MutableLiveData<Boolean>()
    val navigateToForgotPassword: LiveData<Boolean> = _navigateToForgotPassword
    fun onForgotPasswordClicked() {
        _navigateToForgotPassword.value = true
    }
    fun onNavigated() {
        _navigateToForgotPassword.value = false
    }


}