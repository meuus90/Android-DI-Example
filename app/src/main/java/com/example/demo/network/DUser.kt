package com.example.demo.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DUser(val id: String,
                 val name: String,
                 val phone: String,
                 val birthDay: String) : Parcelable