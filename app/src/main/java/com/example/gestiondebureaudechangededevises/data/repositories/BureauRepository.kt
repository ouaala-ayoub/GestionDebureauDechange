package com.example.gestiondebureaudechangededevises.data.repositories

import com.example.gestiondebureaudechangededevises.data.models.Bureau
import com.example.gestiondebureaudechangededevises.data.models.MessageResponse
import com.example.gestiondebureaudechangededevises.data.remote.Retrofit
import com.example.gestiondebureaudechangededevises.data.remote.RetrofitService
import retrofit2.Call

class BureauRepository(private val retrofitService: RetrofitService) {

    fun getBureaux(): Call<List<Bureau>> {
        return retrofitService.getBureaux()
    }

    fun getBureauById(bureauId: String): Call<Bureau> {
        return retrofitService.getBureauById(bureauId)
    }

    fun addBureau(bureau: Bureau): Call<Bureau> {
        return retrofitService.addBureau(bureau)
    }

    fun deleteBureau(bureauId: String): Call<String> {
        return retrofitService.deleteBureau(bureauId)
    }

    fun putBureau(bureauId: String, bureau: Bureau): Call<String> {
        return retrofitService.putBureau(bureauId, bureau)
    }

    companion object {
        private var repo: BureauRepository? = null
        fun getInstance(): BureauRepository {
            if (repo == null) {
                repo = BureauRepository(Retrofit.getInstance())
            }
            return repo!!
        }
    }

}