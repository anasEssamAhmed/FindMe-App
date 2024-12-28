package com.example.findmeapp.model

import android.net.Uri
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class Repository(private val firebaseAuth: FirebaseAuth) {
    private val db = FirebaseFirestore.getInstance()

    suspend fun createAccount(email: String, password: String): FirebaseUser? {
        return try {
            val result: AuthResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun signIn(email: String, password: String): FirebaseUser? {
        return try {
            val result: AuthResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun saveUserDataToFiretore(userId: String, name: String, address: String): Boolean {
        return try {
            val user = hashMapOf(
                "name" to name,
                "address" to address
            )
            db.collection("users")
                .document(userId)
                .set(user)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun resetPassword(email: String) : Boolean{
        val response = firebaseAuth.sendPasswordResetEmail(email)
        return response.isSuccessful
    }

    suspend fun uploadImageToFirebase(imageUri: Uri, userId: String): String? {
        return try {
            val storageReference = FirebaseStorage.getInstance().reference
                .child("users/$userId/profile_image.jpg")
            val uploadTask = storageReference.putFile(imageUri).await()
            val downloadUrl = storageReference.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}