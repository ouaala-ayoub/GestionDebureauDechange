package com.example.gestiondebureaudechangededevises.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class RequestInterceptor : Interceptor {

    companion object {
        private const val TAG = "RequestInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        //add the session cookie stored in the shared prefs
        SessionCookie.prefs.get()?.apply {
            Log.d(TAG, "session_cookie = $this")
            builder.addHeader("Cookie", "admin-session=$this")
        }

        return chain.proceed(builder.build())
    }
}
