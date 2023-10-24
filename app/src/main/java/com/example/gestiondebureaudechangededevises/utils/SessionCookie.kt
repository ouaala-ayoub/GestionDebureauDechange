package com.example.gestiondebureaudechangededevises.utils

import com.example.gestiondebureaudechangededevises.R

class SessionCookie {
    companion object {
        private const val name = "cookie"
        private const val keyRes = R.string.cookie_token
        val prefs = PrefsCRUD(name, keyRes)
    }
}