package com.example.gestiondebureaudechangededevises.data.remote

import com.example.gestiondebureaudechangededevises.data.models.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    /////////////////////////admins auth//////////////////////
    @POST("admins/auth")
    fun getAuth(): Call<Admin>

    @POST("admins/sign")
    fun login(@Body credentials: AdminCredentials): Call<Admin?>

    //////////////////////////////////////////////////

    /////////////////////////bureaux////////////////////
    @GET("desks")
    fun getBureaux(): Call<List<Bureau>>

    @GET("desks/{id}")
    fun getBureauById(@Path("id") bureauId: String): Call<Bureau>

    @POST("desks")
    fun addBureau(@Body bureau: Bureau): Call<Bureau>

    @DELETE("desks/{id}")
    fun deleteBureau(@Path("id") bureauId: String): Call<String>

    @PUT("desks/{id}")
    fun putBureau(@Path("id") bureauId: String, @Body bureau: Bureau): Call<String>
    ////////////////////////////////////////////////////

    /////////////////////////transactions////////////////
    @GET("desks/{id}/transactions")
    fun getBureauxTransactions(@Path("id") bureauxId: String): Call<List<TransactionSchema>>

    @GET("transactions")
    fun getAllTransactions(): Call<List<Transaction>>

    ///////////////////////////////////////////////////


    /////////////////////////users////////////////////
    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("users/{id}")
    fun getUserById(@Path("id") userId: String): Call<User>

    @POST("users")
    fun addUser(@Body user: User): Call<User>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") userId: String): Call<String>

    @PUT("users/{id}")
    fun putUser(@Path("id") userId: String, @Body user: User): Call<String>

    @PUT("users/{id}")
    fun putPassword(
        @Path("id") userId: String,
        @Body passwordRequest: PasswordRequest
    ): Call<String>
    ///////////////////////////////////////////////////
}