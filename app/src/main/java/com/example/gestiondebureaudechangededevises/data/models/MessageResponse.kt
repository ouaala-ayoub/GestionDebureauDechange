package com.example.gestiondebureaudechangededevises.data.models

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message") val message: String
)
