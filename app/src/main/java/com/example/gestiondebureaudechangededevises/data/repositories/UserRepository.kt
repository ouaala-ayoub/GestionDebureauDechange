package com.example.gestiondebureaudechangededevises.data.repositories

import com.example.gestiondebureaudechangededevises.data.models.MessageResponse
import com.example.gestiondebureaudechangededevises.data.models.PasswordRequest
import com.example.gestiondebureaudechangededevises.data.models.User
import com.example.gestiondebureaudechangededevises.data.remote.Retrofit
import com.example.gestiondebureaudechangededevises.data.remote.RetrofitService
import retrofit2.Call

class UserRepository(private val retrofitService: RetrofitService) {

    fun getUsers(): Call<List<User>> {
        return retrofitService.getUsers()
    }

    fun getUserById(userId: String): Call<User> {
        return retrofitService.getUserById(userId)
    }

    fun addUser(user: User): Call<User> {
        return retrofitService.addUser(user)
    }

    fun deleteUser(userId: String): Call<String> {
        return retrofitService.deleteUser(userId)
    }

    fun putUser(userId: String, user: User): Call<String> {
        return retrofitService.putUser(userId, user)
    }

    fun putPassword(userId: String, newPassword: String) =
        retrofitService.putPassword(userId, PasswordRequest(newPassword))

    companion object {
        private var repo: UserRepository? = null
        fun getInstance(): UserRepository {
            if (repo == null) {
                repo = UserRepository(Retrofit.getInstance())
            }
            return repo!!
        }
    }
}