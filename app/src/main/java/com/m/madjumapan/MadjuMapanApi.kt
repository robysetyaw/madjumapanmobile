package com.m.madjumapan


import com.m.madjumapan.ui.admin.transactions.TransactionsResponse
import com.m.madjumapan.ui.admin.user.InsertUserResponse
import com.m.madjumapan.ui.gudang.ItemNamesResponse
import com.m.madjumapan.ui.gudang.UserResponse
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
                         @Query("status") status: String?,
                         @Query("date_filter_type") dateFilterType: String = "between",
                         @Query("per_person_type")perPersonType : String?,
                         @Query("per_person_id")perPersonId: String? ): Call<TransactionsResponse>



    @GET("api/gudang/items/names")
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun getItemNames( @Header("Authorization") auth: String,): Call<ItemNamesResponse>

    @GET("api/users/names")
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun getUserName(@Header("Authorization") auth: String,
                    @Query("role") role: String): Call<UserResponse>

    @POST("api/gudang/insert")
    fun insertItem(@Header("Authorization") auth: String,
                   @Body body: RequestBody): Call<LoginResponse>



    @GET("api/gudang/items/weight")
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun getItemWeights( @Header("Authorization") auth: String,
                        @Query("date1")date1: String,
                        @Query("item_name")itemName: String): Call<StockWeightTodayResponse>



    @POST("api/users/insert")
    fun insertUser(@Header("Authorization") auth: String,
                   @Body body: RequestBody): Call<InsertUserResponse>

}