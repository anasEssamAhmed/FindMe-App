package com.example.findmeapp.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmeapp.model.Post
import com.example.findmeapp.model.Repository
import com.example.findmeapp.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    val user = MutableLiveData<User>()
    val post = MutableLiveData<Post>()
    val statusMessage = MutableLiveData<String>()
    private val isLoading = MutableLiveData<Boolean>()
    private val auth = FirebaseAuth.getInstance()
    val navigateToLogin = MutableLiveData<Boolean>()
    val navigateToMain = MutableLiveData<Boolean>()
    val imageUri = MutableLiveData<Uri>()
    val missingPersonImageUrl = MutableLiveData<Uri>()

    init {
        user.value = User()
        navigateToLogin.postValue(false)
    }

    fun updateDataUserInfo(id: String, name: String, gmail: String, address: String) {

        viewModelScope.launch {
            try {
                repository.updateDataUserInfo(id, name, gmail, address)
                Log.d("TAGMo7ista", "updateDataUserInfo: true")
                statusMessage.value = "update successful"
            } catch (e: Exception) {
                statusMessage.value = e.message
                Log.d("TAGMo7ista", "updateDataUserInfo: true")
            }
        }

    }

    fun createAccount() {
        val currentUser = user.value

        if (currentUser != null) {
            if (currentUser.name.isEmpty() || currentUser.email.isEmpty() || currentUser.address.isEmpty() || currentUser.password.isEmpty() || currentUser.imgUrl.isEmpty()) {
                statusMessage.value = "جميع الحقول مطلوبة"
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentUser.email).matches()) {
                statusMessage.value = "البريد الإلكتروني غير صحيح"
            } else if (currentUser.password.length < 6) {
                statusMessage.value = "كلمة المرور يجب أن تكون على الأقل 6 أحرف"
            } else {
                isLoading.value = true
                viewModelScope.launch {
                    try {
                        val user = repository.createAccount(currentUser.email, currentUser.password)
                        repository.saveUserDataToFiretore(
                            user!!.uid,
                            currentUser.name,
                            currentUser.email,
                            currentUser.address,
                            currentUser.password,
                            currentUser.imgUrl
                        )
                        statusMessage.value = "تم إنشاء الحساب بنجاح!"
                        navigateToLogin.postValue(true)
                        if (imageUri.value != null) {
                            uploadImageToFirebase(imageUri.value!!, user.uid)
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


    fun getUserInfo(){
        viewModelScope.launch {
            val currentUser = repository.getInformation()
            user.value!!.id = currentUser!!.id
            user.value?.name = currentUser.name
            user.value?.email = currentUser.email
            user.value?.address = currentUser.address
            user.value?.password = currentUser.password
            user.value?.imgUrl = currentUser.imgUrl

            Log.d("TAGMo7ista", "getUserInfoMainViewModel: ${user.value}")
        }
    }

    fun createPost(nameMissing:String,ageMissing:String,description: String, missingPersonImage: String, postId: String) {

        if (user.value!!.name.isNotEmpty() || user.value!!.email.isNotEmpty() || user.value!!.address.isNotEmpty() || user.value!!.password.isNotEmpty() || user.value!!.imgUrl.isNotEmpty() || description.isNotEmpty() || missingPersonImage.isNotEmpty() || postId.isNotEmpty()) {
            Log.d(
                "TAGMo7ista",
                "createPostSuccessful: name -> ${user.value!!.name}, email -> ${user.value!!.email}, address -> ${user.value!!.address}, password -> ${user.value!!.password}, imgUrlProfile -> ${user.value!!.imgUrl}, description -> $description,missingPersonImage -> $missingPersonImage, postId -> $postId"
            )
            viewModelScope.launch {
                repository.createPost(user.value!!.id!!, user.value!!.name, user.value!!.email,user.value!!.address,user.value!!.imgUrl,nameMissing,ageMissing,description,missingPersonImage,postId)
            }
            statusMessage.value = "create post Successful"
        } else {
            Log.d(
                "TAGMo7ista",
                "createPostFailed: name -> ${user.value!!.name}, email -> ${user.value!!.email}, address -> ${user.value!!.address}, password -> ${user.value!!.password}, imgUrlProfile -> ${user.value!!.imgUrl}, description -> $description,missingPersonImage -> $missingPersonImage, postId -> $postId"
            )
            statusMessage.value = "create post failed"
        }
    }


    fun getInformation() {
        val currentUser = user.value
        if (currentUser != null) {
            viewModelScope.launch {
                user.value = repository.getInformation()
            }
        }

    }

    fun signIn() {
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


    fun resetPassword() {
        val user = user.value
        viewModelScope.launch {
            try {
                repository.resetPassword(user!!.email)
                statusMessage.value = "تم ارسال رابط اعادة تعيين كلمة المرور"

            } catch (e: Exception) {
                statusMessage.value = "يوجد خطا من طرف التطبيق${e.message}"
            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri, userID: String) {
        viewModelScope.launch {
            val s = repository.uploadImageToFirebase(imageUri, userID)
            Log.d("aaa", s.toString())
        }
    }
}