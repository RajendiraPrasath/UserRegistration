package com.example.userregistration.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (var fullName:String?=null,
                 var userName:String?=null,
                 var email:String?=null,
                 var phoneNo:String?=null,
                 var password:String?=null
) : Parcelable
