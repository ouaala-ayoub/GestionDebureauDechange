package com.example.gestiondebureaudechangededevises.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Bureau(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("name") val name: String,
    @SerializedName("stock") val stock: Map<String, String>?=null,
    @SerializedName("users") val users: List<String> = listOf(),
) : Parcelable

@Parcelize
data class Stock(
    //TODO map will do the job also
    @SerializedName("euro") val euro: String = "0",
    @SerializedName("dollar") val dollar: String = "0",
    @SerializedName("dirham") val dirham: String = "0"
) : Parcelable