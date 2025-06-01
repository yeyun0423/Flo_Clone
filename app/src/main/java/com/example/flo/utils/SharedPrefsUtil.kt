package com.example.flo.utils

import android.content.Context

object SharedPrefsUtil {
    fun getJwt(context: Context): String? {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getString("jwt", null)
    }
}