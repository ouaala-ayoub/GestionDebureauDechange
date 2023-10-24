package com.example.gestiondebureaudechangededevises.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("name") val name: String,
    @SerializedName("phone") var phone: String?=null,
    @SerializedName("email") var email: String?=null,
    @SerializedName("userName") val userName: String?=null,
    @SerializedName("password") val password: String?=null,
    @SerializedName("desk") val desk: String,
) : Parcelable
