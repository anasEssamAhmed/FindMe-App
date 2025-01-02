package com.example.findmeapp.model

import android.graphics.Bitmap

data class User(
    var name: String = "",
    var email: String = "",
    var address: String = "",
    var password: String = "",
    var id: String? = ""
)
