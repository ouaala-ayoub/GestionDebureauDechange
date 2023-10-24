package com.example.gestiondebureaudechangededevises

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class MyApp : Application() {
    companion object {
        private const val TAG = "MyApp"
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        //set the dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        
        appContext = applicationContext

    }
}