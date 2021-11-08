package com.m.madjumapan


import com.m.madjumapan.ui.admin.transactions.TransactionsResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface MadjuMapanApi {

    @POST("api/login")
    fun login(@Body body: RequestBody): Call<LoginResponse>

    @GET("api/gudang/items/stocks")
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun getStock( @Header("Authorization") auth: String): Call<StockResponse>

    @GET("api/gudang/items/transactions")
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun getTransactions( @Header("Authorization") auth: String,
    @Query("date1") date1 : String,
    @Query("date2") date2 : String,
    @Query("date_filter_type") dateFilterType: String = "between"): Call<TransactionsResponse>


//
//    @GET("api/gudang/items/stocks")
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    fun getStock( @Header("Authorization") auth: String): Call<StockResponse>


}