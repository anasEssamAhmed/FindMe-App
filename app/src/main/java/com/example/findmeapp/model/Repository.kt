package com.example.findmeapp.model

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.example.findmeapp.view.main.fragment.chat.data.FriendlyMessage
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class Repository(private val firebaseAuth: FirebaseAuth) {
    private val db = FirebaseFirestore.getInstance()
    val database = Firebase.database
    val myRef = database.getReference("message")

    suspend fun createAccount(email: String, password: String): FirebaseUser? {
        return try {
            val result: AuthResult =
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            throw e
        }
    }


    suspend fun sendMessage(chatId: String, message: FriendlyMessage): Boolean {

        try {
            myRef.child(chatId).push().setValue(message)
                .addOnCompleteListener {
                    Log.d("TAGMo7ista", "sendMessageComplete: ${it.isComplete}")
                }
                .addOnFailureListener {
                    Log.d("TAGMo7ista", "sendMessageFailure: ${it.message}")
                }.await()
            return true
        } catch (e: Exception) {
            Log.e("TAGMo7ista", "sendMessageException => ${e.message}")
            return false
        }


    }

    @SuppressLint("SuspiciousIndentation")
    fun getAllChat(chatId: String, callback: (List<FriendlyMessage>) -> Unit) : MutableList<FriendlyMessage>{
        val messages = mutableListOf<FriendlyMessage>()

        myRef.child(chatId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(FriendlyMessage::class.java)
                    message?.let { messages.add(it) }
                }
                callback(messages)
                Log.d("TAGMo7ista", "onDataChange: => $messages ")
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
        return messages
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

    suspend fun getAllPostFromFirebase(): ArrayList<Post> {
        try {
            val posts = ArrayList<Post>()
            db.collection("Post").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val id = document.data.getValue("userId").toString()
                        val name = document.data.getValue("name").toString()
                        val email = document.data.getValue("email").toString()
                        val address = document.data.getValue("address").toString()
                        val imgUrl = document.data.getValue("imageUri").toString()
                        val nameMissing = document.data.getValue("nameMissing").toString()
                        val ageMissing = document.data.getValue("ageMissing").toString()
                        val description = document.data.getValue("description").toString()
                        val missingPersonImage =
                            document.data.getValue("missingPersonImage").toString()
                        val idPost = document.data.getValue("postId").toString()
                        posts.add(
                            Post(
                                id,
                                name,
                                email,
                                address,
                                imgUrl,
                                nameMissing,
                                ageMissing,
                                description,
                                missingPersonImage,
                                idPost
                            )
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(
                        "TAGMo7ista",
                        "getAllPostFromFirebaseOnFailure: Error getting documents",
                        exception
                    )
                }.await()
            Log.e("TAGMo7ista", "getAllPostFromFirebaseOnSuccess: $posts")
            return posts
        } catch (e: Exception) {
            Log.e("TAGMo7ista", "getAllPostFromFirebaseException: $e")
            return ArrayList<Post>()
        }

    }


    suspend fun getMyPostFromFirebase(): ArrayList<Post> {
        try {
            val userId = firebaseAuth.currentUser!!.uid
            val posts = ArrayList<Post>()
            db.collection("Post").whereEqualTo("userId", userId).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val id = document.data.getValue("userId").toString()
                        val name = document.data.getValue("name").toString()
                        val email = document.data.getValue("email").toString()
                        val address = document.data.getValue("address").toString()
                        val imgUrl = document.data.getValue("imageUri").toString()
                        val nameMissing = document.data.getValue("nameMissing").toString()
                        val ageMissing = document.data.getValue("ageMissing").toString()
                        val description = document.data.getValue("description").toString()
                        val missingPersonImage =
                            document.data.getValue("missingPersonImage").toString()
                        val idPost = document.data.getValue("postId").toString()

                        posts.add(
                            Post(
                                id,
                                name,
                                email,
                                address,
                                imgUrl,
                                nameMissing,
                                ageMissing,
                                description,
                                missingPersonImage,
                                idPost
                            )
                        )

                    }
                    Log.e("TAGMo7ista", "getMyPostFromFirebaseOnSuccess: $posts")
                }
                .addOnFailureListener { exception ->
                    Log.w(
                        "TAGMo7ista",
                        "getMyPostFromFirebaseOnFailure: Error getting documents",
                        exception
                    )
                }.await()
            Log.e("TAGMo7ista", "getMyPostFromFirebaseOnSuccess: $posts")
            return posts
        } catch (e: Exception) {
            Log.e("TAGMo7ista", "getMyPostFromFirebaseException: $e")
            return ArrayList<Post>()
        }

    }

    suspend fun getPostById(postId: String): Post {
        try {
            val post = Post()
            db.collection("Post").document(postId).get()
                .addOnSuccessListener { result ->
                    val id = result.data?.getValue("userId").toString()
                    val name = result.data?.getValue("name").toString()
                    val email = result.data?.getValue("email").toString()
                    val address = result.data?.getValue("address").toString()
                    val imgUrl = result.data?.getValue("imageUri").toString()
                    val nameMissing = result.data?.getValue("nameMissing").toString()
                    val ageMissing = result.data?.getValue("ageMissing").toString()
                    val description = result.data?.getValue("description").toString()
                    val missingPersonImage = result.data?.getValue("missingPersonImage").toString()
                    val idPost = result.data?.getValue("postId").toString()
                    post.id = id
                    post.name = name
                    post.email = email
                    post.address = address
                    post.imgUrl = imgUrl
                    post.nameMissing = nameMissing
                    post.ageMissing = ageMissing
                    post.description = description
                    post.missingPersonImage = missingPersonImage
                    post.idPost = idPost
                }.addOnFailureListener { exception ->
                    Log.w(
                        "TAGMo7ista",
                        "getMyPostFromFirebaseOnFailure: Error getting documents",
                        exception
                    )
                }.await()
            return post
        } catch (e: Exception) {
            Log.e("TAGMo7ista", "getPostById: Exception => $e")
            return Post()
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
        Log.d("TAGMo7dita", "signOut")
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

    suspend fun updatePost(
        nameMissing: String,
        ageMissing: String,
        description: String,
        missingPersonImage: String,
        postId: String
    ): Boolean {
        return try {
            if (nameMissing.isNotEmpty() && ageMissing.isNotEmpty() && description.isNotEmpty() && missingPersonImage.isNotEmpty() && postId.isNotEmpty()) {
                val postData = hashMapOf<String, Any>(
                    "nameMissing" to nameMissing,
                    "ageMissing" to ageMissing,
                    "description" to description,
                    "missingPersonImage" to missingPersonImage,
                )
                db.collection("Post").document(postId).update(postData).await()
            }
            true
        } catch (e: Exception) {
            Log.e("TAGMo7ista", "updatePost: Exception => $e ")
            false
        }
    }

    suspend fun createPost(
        userId: String,
        name: String,
        email: String,
        address: String,
        imageUri: String,
        nameMissing: String,
        ageMissing: String,
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
                "nameMissing" to nameMissing,
                "ageMissing" to ageMissing,
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