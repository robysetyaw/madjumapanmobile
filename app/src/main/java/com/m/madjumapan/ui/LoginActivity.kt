package com.m.madjumapan.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.m.madjumapan.*
import com.m.madjumapan.SharePreferencesClient.PERSONAL_TOKEN
import com.m.madjumapan.SharePreferencesClient.ROLE
import com.m.madjumapan.SharePreferencesClient.redirect
import com.m.madjumapan.SharePreferencesClient.sharePreferences
import com.m.madjumapan.databinding.ActivityLoginBinding
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (redirect(this)) finish()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setLogin()
    }

    private fun setLogin() {
        binding.apply {
            btLogin.setOnClickListener {
                val username = tlUsername.editText?.text
                val password = tlPassword.editText?.text

                 // 'this' inside 'swap()' will hold the value of 'list'
                if (username == null) showToast(getString(R.string.username_cannot_be_empty))
                else if (password == null) showToast(getString(R.string.password_cannot_be_empty))
                else {
                    val requestBody: RequestBody = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("username", username.toString().trim())
                        .addFormDataPart("password", password.toString().trim())
                        .build()
                    Log.d(TAG, "setLogin: {${username.toString().trim()}")

                    RetrofitClient.retrofitClient()?.create(MadjuMapanApi::class.java)?.login(requestBody)
                        ?.enqueue(object : Callback<LoginResponse?> {
                            override fun onResponse(
                                call: Call<LoginResponse?>?,
                                response: Response<LoginResponse?>?
                            ) {
                                if (response?.isSuccessful == true) {
                                    val bodyMessage  = response.body()?.message
                                    val tokenMessage = bodyMessage?.token
                                    val tokenSplit = tokenMessage?.split("|")
                                    val token = tokenSplit?.get(1)
                                    var role = ""
                                    when {
                                        bodyMessage?.isAdmin == 1 -> role = "isAdmin"
                                        bodyMessage?.isGudang == 1 -> role = "isGudang"
                                        bodyMessage?.isCustomer == 1 -> role = "isCustomer"
                                        bodyMessage?.isSupplier == 1 -> role = "isSupplier"
                                    }
                                    with(sharePreferences(this@LoginActivity)?.edit()) {
                                        this?.putString(PERSONAL_TOKEN,token )
                                        this?.putString(ROLE, role)
                                        this?.apply()
                                    }
                                    Log.d(TAG, "onResponse: ${response.isSuccessful}")
                                    Log.d(TAG, "onResponse: $response")
                                    showToast(token.toString())
                                    redirect(this@LoginActivity)
                                    finish()
                                } else {
                                    val failedMessage = response?.body()?.message?.messageFailed.toString()
                                    Log.d(TAG, "onResponse: ${response?.isSuccessful}")
                                    Log.d(TAG, "onResponse: $response")
                                    showToast(failedMessage)
                                }
                            }

                            override fun onFailure(call: Call<LoginResponse?>?, t: Throwable?) {
                                Log.d(TAG, "onFailure: $t")
                                showToast(t?.message.toString())
                            }
                        })
                }

            }
        }
    }
}

