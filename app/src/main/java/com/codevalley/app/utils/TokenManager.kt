package com.codevalley.app.utils

import android.content.Context
import android.content.SharedPreferences
import com.codevalley.app.repository.UserRepository

object TokenManager {
    private const val PREF_NAME = "token_prefs"
    private const val TOKEN_KEY = "token_key"
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    var token: String?
        get() = preferences.getString(TOKEN_KEY, null)
        set(value) {
            preferences.edit().putString(TOKEN_KEY, value).apply()
        }

    fun clearToken() {
        preferences.edit().remove(TOKEN_KEY).apply()
    }
}
