package com.m.madjumapan

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.m.madjumapan.ui.admin.AdminActivity

object SharePreferencesClient {

    val ROLE: String? = null
    private var sharedPref: SharedPreferences? = null
    const val PERSONAL_TOKEN = "PERSONAL TOKEN"
    fun sharePreferences(context: Context): SharedPreferences? {
        if (sharedPref == null) {
            sharedPref = context.getSharedPreferences("my_token_share", Context.MODE_PRIVATE)
        }
        return sharedPref
    }

    fun redirect(context: Context): Boolean {
        val token = sharePreferences(context)?.getString(PERSONAL_TOKEN, "invalid token")
        val role = sharePreferences(context)?.getString(ROLE, "invalid role")
        if (token != "invalid token" && role != "invalid role") {
            if (role == "isAdmin") {
                val intent = Intent(context, AdminActivity::class.java )
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                return true
            }
            // todo gudang

        } else {
            return false
        }
        return false
    }
}