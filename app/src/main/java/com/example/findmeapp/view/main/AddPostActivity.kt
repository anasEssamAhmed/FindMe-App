package com.example.findmeapp.view.main

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.findmeapp.R
import com.example.findmeapp.databinding.ActivityAddPostBinding
import com.example.findmeapp.model.Repository
import com.example.findmeapp.viewModel.MainViewModel
import com.example.findmeapp.viewModel.ViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding
    private lateinit var repository: Repository
    private var fileURI: Uri? = null
    var listener: ActivityResultLauncher<Intent>? = null
    private val PICK_IMAGE_REQUEST = 999
    private var imageURI: Uri? = null
    private val TAG = "TAGMo7ista"
    private lateinit var dialog: ProgressDialog
    private val auth = FirebaseAuth.getInstance()
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private val imageRef: StorageReference = storageRef.child("posts")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_post)
        repository = Repository(FirebaseAuth.getInstance())
        dialog = ProgressDialog(this)
        val viewModel: MainViewModel by viewModels {
            ViewModelFactory(repository)
        }
        binding.addPostActivity = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.getUserInfo()

        viewModel.statusMessage.observe(this) { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }



        listener = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageURI = result.data?.data
                binding.ivMissingPerson.setImageURI(imageURI)

                dialog = ProgressDialog.show(
                    this, "",
                    "Loading. Please wait...", true
                )
                val bitmap = (binding.ivMissingPerson.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val data = baos.toByteArray()

                val childRef =
                    imageRef.child(System.currentTimeMillis().toString() + "_mo7images.png")

                val uploadTask = childRef.putBytes(data)
                uploadTask
                    .addOnFailureListener { exception ->
                        Log.e(TAG, exception.message!!)
                    }
                    .addOnSuccessListener { taskSnapshot ->
                        Log.e(TAG, "Image Uploaded Successfully : ${taskSnapshot.storage}")
                        Toast.makeText(this, "Image Uploaded Successfully", Toast.LENGTH_SHORT)
                            .show()
                        childRef.downloadUrl
                            .addOnSuccessListener { uri ->
                                Log.e(TAG, uri.toString())
                                fileURI = uri
                                dialog.dismiss()
                            }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "please try again", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "AddPostActivityOnCreate: Error -> $e")

                    }

                Log.e(TAG, imageURI.toString())
            }
        }


        binding.ivMissingPerson.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val isGRanted =
                    checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                if (isGRanted != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        PICK_IMAGE_REQUEST
                    )
                } else {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    listener!!.launch(intent)
                }
            }
        }

        binding.SendPost.setOnClickListener {
            val nameMissing = binding.nameMissing.text.toString()
            val ageMissing = binding.ageMissing.text.toString()
            val description = binding.textFieldDescription.text
            val missingImagePerson = fileURI.toString()
            val postID = auth.currentUser!!.uid + System.currentTimeMillis().toString()
            viewModel.createPost(nameMissing,ageMissing,description.toString(), missingImagePerson, postID)

        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            999 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    listener!!.launch(intent)


                } else {
                    finish()
                }
            }
        }
    }

}