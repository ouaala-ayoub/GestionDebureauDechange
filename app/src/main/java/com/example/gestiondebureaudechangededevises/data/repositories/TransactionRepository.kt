package com.example.gestiondebureaudechangededevises.data.repositories

import com.example.gestiondebureaudechangededevises.data.models.Transaction
import com.example.gestiondebureaudechangededevises.data.models.TransactionSchema
import com.example.gestiondebureaudechangededevises.data.remote.Retrofit
import com.example.gestiondebureaudechangededevises.data.remote.RetrofitService
import retrofit2.Call

class TransactionRepository(private val retrofitService: RetrofitService) {

    fun getBureauxTransactions(bureauxId: String): Call<List<TransactionSchema>> {
        return retrofitService.getBureauxTransactions(bureauxId)
    }

    fun getAllTransactions(): Call<List<Transaction>> {
        return retrofitService.getAllTransactions()
    }

    companion object {
        private var repo: TransactionRepository? = null
        fun getInstance(): TransactionRepository {
            if (repo == null) {
                repo = TransactionRepository(Retrofit.getInstance())
            }
            return repo!!
        }
    }

}