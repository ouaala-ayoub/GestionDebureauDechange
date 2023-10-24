package com.example.gestiondebureaudechangededevises.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class ResponseInterceptor : Interceptor {
    companion object {
        private const val TAG = "ResponseInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        // Check if the response contains a Set-Cookie header
        val cookies = originalResponse.headers("set-cookie")
        Log.d(TAG, "cookies: $cookies")
        if (cookies.isNotEmpty()) {
            for (cookie in cookies) {
                // Handle the session cookie here
                if (cookie.startsWith("admin-session=")) {
                    val sessionCookie = cookie.substringAfter("admin-session=")
                    val sessionValue = sessionCookie.split(";")[0]
                    // Store or process the session cookie as needed
                    Log.d(TAG, "admin-session = $sessionValue")
                    SessionCookie.prefs.set(sessionValue)
                }
            }
        }

        return originalResponse
    }
}