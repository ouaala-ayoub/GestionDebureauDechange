package com.example.gestiondebureaudechangededevises.data.models

import com.google.gson.annotations.SerializedName

data class Error(
    val message: String?,
    val code: Int
)
data class ErrorResponse(
    @SerializedName("message")
    val message: String
)
