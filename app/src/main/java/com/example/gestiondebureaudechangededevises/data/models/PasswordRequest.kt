package com.example.gestiondebureaudechangededevises.data.models

import com.google.gson.annotations.SerializedName

data class PasswordRequest(@SerializedName("password") val newPassword: String)
