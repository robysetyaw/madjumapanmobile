package com.m.madjumapan


import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface MadjuMapanApi {

    @POST("api/login")
    fun login(@Body body: RequestBody): Call<LoginResponse>


}