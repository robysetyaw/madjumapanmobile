package com.m.madjumapan

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    var status: String,
    var message: Message
)

data class Message (

    @SerializedName("token") val token : String?,
    @SerializedName("is_gudang") val isGudang : Int?,
    @SerializedName("is_customer") val isCustomer : Int?,
    @SerializedName("is_admin") val isAdmin : Int?,
    @SerializedName("is_supplier") val isSupplier : Int?,
    @SerializedName("message_failed") val messageFailed : String?
)