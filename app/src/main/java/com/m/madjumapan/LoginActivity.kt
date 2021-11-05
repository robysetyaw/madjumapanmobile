package com.m.madjumapan

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.m.madjumapan.SharePreferencesClient.sharePreferences
import com.m.madjumapan.databinding.ActivityLoginBinding
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // todo ke halaman utama
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
                        .addFormDataPart("username", tlUsername.editText.toString().trim())
                        .addFormDataPart("password", tlPassword.editText.toString().trim())
                        .build()

                    RetrofitClient.retrofitClient()?.create(MadjuMapanApi::class.java)?.login(requestBody)
                        ?.enqueue(object : Callback<LoginResponse?> {
                            override fun onResponse(
                                call: Call<LoginResponse?>?,
                                response: Response<LoginResponse?>?
                            ) {
                                if (response?.isSuccessful == true) {
                                    val tokenMessage = response.body()?.message?.token
                                    val tokenSplit = tokenMessage?.split("|")
                                    val token = tokenSplit?.get(1)
                                    with(sharePreferences(this@LoginActivity)?.edit()) {
                                        this?.putString(token,"invalid token" )
                                        this?.apply()
                                    }
                                    showToast(token.toString())
                                } else {
                                    val failedMessage = response?.body()?.message?.messageFailed.toString()
                                    showToast(failedMessage)
                                }
                            }

                            override fun onFailure(call: Call<LoginResponse?>?, t: Throwable?) {
                                showToast(t?.message.toString())
                            }
                        })
                }

            }
        }
    }
}

