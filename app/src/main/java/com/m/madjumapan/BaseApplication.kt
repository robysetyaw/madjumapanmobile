package com.m.madjumapan

import android.app.Application

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // build retrofit
        RetrofitClient.retrofitClient()
    }
}