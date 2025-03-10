package com.example.findmeapp.model

import android.os.Parcel
import android.os.Parcelable

data class UserList(
    var chatId:String,
    var user: User
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readParcelable<User>(User::class.java.classLoader)!!
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(chatId)
        parcel.writeParcelable(user, flags)
    }


    companion object CREATOR : Parcelable.Creator<UserList> {
        override fun createFromParcel(parcel: Parcel): UserList {
            return UserList(parcel)
        }

        override fun newArray(size: Int): Array<UserList?> {
            return arrayOfNulls(size)
        }
    }
}
