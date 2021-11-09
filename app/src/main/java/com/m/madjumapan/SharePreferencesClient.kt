package com.m.madjumapan

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.edit
import com.m.madjumapan.ui.LoginActivity
import com.m.madjumapan.ui.admin.AdminActivity
import com.m.madjumapan.ui.gudang.GudangActivity

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

    fun logout(context: Context): Boolean {
        sharePreferences(context)?.edit {
            this.remove(PERSONAL_TOKEN)
            this.remove(ROLE)
            apply()
        }
        val intent = Intent(context, LoginActivity::class.java )
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        return true
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
            } else if (role == "isGudang") {
                val intent = Intent(context, GudangActivity::class.java )
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