package com.example.sharedoc.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPrefsHelper {
    private const val PREFS_NAME = "ShareDocPrefs"
    private const val TOKEN_KEY = "auth_token"

    fun saveToken(context: Context, token: String) {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(context: Context): String? {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getString(TOKEN_KEY, null)
    }

    fun clearToken(context: Context) {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().remove(TOKEN_KEY).apply()
    }
}