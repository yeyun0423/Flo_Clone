package com.example.flo

import android.content.Context

object SharedPrefsUtil {
    fun saveJwt(context: Context, userId: Int) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().putInt("jwt", userId).apply()
    }

    fun getJwt(context: Context): Int {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getInt("jwt", -1)
    }

    fun clearJwt(context: Context) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().remove("jwt").apply()
    }
}