package com.m.madjumapan

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    var status: String,
    var message: Message
)

data class Message (

    @SerializedName("token") val token : String?,
    @SerializedName("message_failed") val messageFailed : String?
)