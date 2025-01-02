package com.example.findmeapp.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmeapp.model.Repository
import com.example.findmeapp.model.User
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    val user = MutableLiveData<User>()
    val statusMessage = MutableLiveData<String>()
    private val isLoading = MutableLiveData<Boolean>()
    val navigateToLogin = MutableLiveData<Boolean>()
    val navigateToMain = MutableLiveData<Boolean>()
    val imageUri = MutableLiveData<Uri>()

    init {
        user.value = User()
        navigateToLogin.postValue(false)
    }

    fun createAccount() {
        val currentUser = user.value

        if (currentUser != null) {
            if (currentUser.name.isEmpty() || currentUser.email.isEmpty() ||
                currentUser.address.isEmpty() || currentUser.password.isEmpty()
            ) {
                statusMessage.value = "جميع الحقول مطلوبة"
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentUser.email).matches()) {
                statusMessage.value = "البريد الإلكتروني غير صحيح"
            } else if (currentUser.password.length < 6) {
                statusMessage.value = "كلمة المرور يجب أن تكون على الأقل 6 أحرف"
            } else {
                isLoading.value = true
                viewModelScope.launch {
                    try {
                        val s = repository.createAccount(currentUser.email, currentUser.password)
                        repository.saveUserDataToFiretore(s!!.uid,currentUser.name, currentUser.address)
                        statusMessage.value = "تم إنشاء الحساب بنجاح!"
                        navigateToLogin.postValue(true)
                        if (imageUri.value != null){
                            uploadImageToFirebase(imageUri.value!!, s.uid)
                        }
                    } catch (e: Exception) {
                        statusMessage.value = "فشل في إنشاء الحساب: ${e.message}"
                    } finally {
                        isLoading.value = false
                    }
                }
            }
        }
    }

    fun signIn(){
        val currentUser = user.value
        if (currentUser != null) {
            if (currentUser.email.isEmpty() || currentUser.password.isEmpty()) {
                statusMessage.value = "جميع الحقول مطلوبة"
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentUser.email).matches()) {
                statusMessage.value = "البريد الإلكتروني غير صحيح"
            } else {
                viewModelScope.launch {
                    try {
                        val s = repository.signIn(currentUser.email, currentUser.password)
                        statusMessage.value = "تم تسجيل الدخول بنجاح!"
                        navigateToMain.postValue(true)
                    } catch (e: Exception) {
                        statusMessage.value = "فشل في تسجيل الدخول: ${e.message}"
                    }
                }
            }
        }
    }

    fun resetPassword(){
        val user = user.value
        viewModelScope.launch {
            try {
                repository.resetPassword(user!!.email)
                statusMessage.value = "تم ارسال رابط اعادة تعيين كلمة المرور"

            }catch (e : Exception){
                statusMessage.value = "يوجد خطا من طرف التطبيق${e.message}"
            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri, userID : String) {
        viewModelScope.launch {
           val s = repository.uploadImageToFirebase(imageUri,userID)
            Log.d("aaa" , s.toString())
        }
    }
}