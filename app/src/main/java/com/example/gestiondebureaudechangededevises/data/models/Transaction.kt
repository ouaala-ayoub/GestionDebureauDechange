package com.example.gestiondebureaudechangededevises.data.models

import com.google.gson.annotations.SerializedName

data class Transaction(
    @SerializedName("_id") val id: String,
    @SerializedName("in") val inDevice: InOut,
    @SerializedName("out") val out: InOut
)

//TODO add in and out classes
data class InOut(
    @SerializedName("amount") val amount: String,
    @SerializedName("currency") val currency: String
)

data class TransactionSchema(
    @SerializedName("_id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("currencyValue") val currencyValue: Double,
    @SerializedName("desk") val desk: String
)

enum class TransactionType(val value: String) {
    BUY("achat"),
    SELL("vente"),
}