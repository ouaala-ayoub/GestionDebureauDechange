package com.example.gestiondebureaudechangededevises.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.gestiondebureaudechangededevises.MyApp

open class PrefsCRUD(name: String, private val keyRes: Int) {

    private val sharedPrefs: SharedPreferences =
        MyApp.appContext.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun set(value: String) {
        val context = MyApp.appContext
        with(sharedPrefs.edit()) {
            putString(context.resources.getString(keyRes), value)
            commit()
        }
    }

    fun get(): String? {
        val accessToken: String?
        val context = MyApp.appContext
        context.apply {
            accessToken = sharedPrefs
                .getString(
                    resources.getString(keyRes), null
                )
        }
        return accessToken
    }

    fun delete(): Boolean {
        try {
            val context = MyApp.appContext

            val editor = sharedPrefs.edit()
            editor
                .remove(context.resources.getString(keyRes))
                .apply()
        } catch (e: Throwable) {
            e.printStackTrace()
            return false
        }
        return true
    }

}