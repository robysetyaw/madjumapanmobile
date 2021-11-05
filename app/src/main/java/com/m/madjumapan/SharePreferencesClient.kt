package com.m.madjumapan

import android.content.Context
import android.content.SharedPreferences

object SharePreferencesClient {

    private var sharedPref: SharedPreferences? = null

    fun sharePreferences(context: Context): SharedPreferences? {
        if (sharedPref == null) {
            sharedPref = context.getSharedPreferences("my_token_share", Context.MODE_PRIVATE)
        }
        return sharedPref
    }
}