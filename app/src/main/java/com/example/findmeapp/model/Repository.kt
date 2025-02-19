package com.example.findmeapp.model

import android.net.Uri
import android.util.Log
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
            val result: AuthResult =
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun signIn(email: String, password: String): FirebaseUser? {
        return try {
            val result: AuthResult =
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getInformation(): User? {
        try {

            val documentSnapshot =
                db.collection("users").document(firebaseAuth.currentUser!!.uid).get().await()
            if (documentSnapshot.exists()) {
                val uid = documentSnapshot.getString("userId")
                val name = documentSnapshot.getString("name")
                val email = documentSnapshot.getString("email")
                val address = documentSnapshot.getString("address")
                val password = documentSnapshot.getString("password")
                val imageUri = documentSnapshot.getString("imageUri")
                return User(uid, name!!, email!!, address!!, password!!, imageUri!!)
            }
            return null
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    suspend fun signOut() {
        firebaseAuth.signOut()
    }


    suspend fun updateDataUserInfo(
        userId: String,
        name: String,
        email: String,
        address: String
    ): Boolean {

        return try {
            if (userId.isNotEmpty() && name.isNotEmpty() && email.isNotEmpty() && address.isNotEmpty()) {
                val userData = hashMapOf<String, Any>(
                    "name" to name,
                    "email" to email,
                    "address" to address
                )
                db.collection("users").document(userId).update(userData).await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun saveUserDataToFiretore(
        userId: String,
        name: String,
        email: String,
        address: String,
        password: String,
        imageUri: String
    ): Boolean {
        return try {
            val user = hashMapOf(
                "userId" to userId,
                "name" to name,
                "email" to email,
                "address" to address,
                "password" to password,
                "imageUri" to imageUri
            )
            db.collection("users").document(userId).set(user).await()
            true
        } catch (e: Exception) {
            false
        }
    }


    suspend fun createPost(
        userId: String,
        name: String,
        email: String,
        address: String,
        imageUri: String,
        description: String,
        missingPersonImage: String,
        postId: String
    ): Boolean {

        return try {
            val user = hashMapOf(
                "userId" to userId,
                "name" to name,
                "email" to email,
                "address" to address,
                "imageUri" to imageUri,
                "description" to description,
                "missingPersonImage" to missingPersonImage,
                "postId" to postId
            )


            Log.d("TAGMo7ista", "createPostRepository: $user")
            db.collection("Post").document(postId).set(user).await()
            true
        } catch (e: Exception) {
            false
        }


    }


    suspend fun resetPassword(email: String): Boolean {
        val response = firebaseAuth.sendPasswordResetEmail(email)
        return response.isSuccessful
    }

    suspend fun uploadImageToFirebase(imageUri: Uri, userId: String): String? {
        return try {
            val storageReference =
                FirebaseStorage.getInstance().reference.child("users/$userId/profile_image.jpg")
            storageReference.putFile(imageUri).await()
            val downloadUrl = storageReference.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}