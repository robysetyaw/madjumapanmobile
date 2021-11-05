package com.m.madjumapan

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class LoginActivityTest {
    @Test
    fun loginTest() {

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", "admin")
            .addFormDataPart("password", "admin")
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
                        print(response.body())
                        assertNotNull(response.body())
                    } else {
                        val failedMessage = response?.body()?.message?.messageFailed.toString()
                        print(failedMessage)
                    }
                }

                override fun onFailure(call: Call<LoginResponse?>?, t: Throwable?) {
                    print(t)
                    assertFalse(t?.message.toString(), true)
                }
            })
    }
}