package com.example.findmeapp.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class User(
    var id: String? = "",
    var name: String = "",
    var email: String = "",
    var address: String = "",
    var password: String = "",
    var imgUrl: String = ""
): Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
    )

    override fun describeContents(): Int {
        return  0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(address)
        parcel.writeString(password)
        parcel.writeString(imgUrl)
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
