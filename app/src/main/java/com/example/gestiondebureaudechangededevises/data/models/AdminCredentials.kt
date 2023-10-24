package com.example.gestiondebureaudechangededevises.data.models

import com.google.gson.annotations.SerializedName

data class AdminCredentials(
    @SerializedName("userName") val username: String,
    @SerializedName("password") val password: String,
)
