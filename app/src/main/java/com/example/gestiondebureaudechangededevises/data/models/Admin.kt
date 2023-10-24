package com.example.gestiondebureaudechangededevises.data.models

import com.google.gson.annotations.SerializedName

data class Admin(
    @SerializedName("_id") val id: String?=null,
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: Phone?=null,
    @SerializedName("email") val email: String?=null,
    @SerializedName("userName") val userName: String,
    @SerializedName("password") val password: String,
)
data class Phone(
    @SerializedName("code") val code: String,
    @SerializedName("number") val number: String,
)