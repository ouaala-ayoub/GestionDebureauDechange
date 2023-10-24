package com.example.gestiondebureaudechangededevises.data.remote

import com.example.gestiondebureaudechangededevises.utils.RequestInterceptor
import com.example.gestiondebureaudechangededevises.utils.ResponseInterceptor
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {
    //TODO the base url
    private const val BASE_URL = "https://desk-change.vercel.app/api/"
    private var retrofitService: RetrofitService? = null


    fun getInstance(): RetrofitService {
        if (retrofitService == null) {
            val client = OkHttpClient
                .Builder()
                .addInterceptor(RequestInterceptor())
                .addInterceptor(ResponseInterceptor())
            val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
            retrofitService = retrofit.create(RetrofitService::class.java)
        }
        return retrofitService!!
    }
}