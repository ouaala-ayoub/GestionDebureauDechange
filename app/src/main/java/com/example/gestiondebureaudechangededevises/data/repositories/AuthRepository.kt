package com.example.gestiondebureaudechangededevises.data.repositories

import com.example.gestiondebureaudechangededevises.data.models.AdminCredentials
import com.example.gestiondebureaudechangededevises.data.remote.Retrofit
import com.example.gestiondebureaudechangededevises.data.remote.RetrofitService

class AuthRepository(private val retrofitService: RetrofitService) {

    fun getAuth() = retrofitService.getAuth()
    fun login(credentials: AdminCredentials) = retrofitService.login(credentials)

    companion object {
        private var repo: AuthRepository? = null
        fun getInstance(): AuthRepository {
            if (repo == null) {
                repo = AuthRepository(Retrofit.getInstance())
            }
            return repo!!
        }
    }
}