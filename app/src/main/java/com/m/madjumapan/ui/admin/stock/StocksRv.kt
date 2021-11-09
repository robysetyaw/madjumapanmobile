package com.m.madjumapan

import android.text.format.Time
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

data class StockResponse(
    val status : String,
    val message: Message
) {
    data class Message(
        @SerializedName("data") var data : List<Stock>
    )
    data class  Stock(
        @SerializedName("item_name") val itemName : String,
        @SerializedName("stock") val stock : String,
    )

}


data class StockWeightTodayResponse(
    val status : String,
    val message: Message
) {
    data class Message(
        @SerializedName("data") var data : List<Stock?>
    )
    data class  Stock(
        @SerializedName("stock_in") val stockIn : String,
        @SerializedName("stock_out") val stockOut : String,
    )

}


class StocksRv(val dataset: ArrayList<StockResponse.Stock> = arrayListOf()) : RecyclerView.Adapter<StocksRv.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun load() {
            itemView.apply {
                val df= SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val date: String = df.format(Calendar.getInstance().time)
                val token = SharePreferencesClient.sharePreferences(context)
                    ?.getString(SharePreferencesClient.PERSONAL_TOKEN, "invalid token")
                findViewById<TextView>(R.id.tvPetugas).text = dataset[adapterPosition].itemName
                findViewById<TextView>(R.id.tvStock).text = "${dataset[adapterPosition].stock} Kg"
                RetrofitClient.retrofitClient()?.create(MadjuMapanApi::class.java)
                    ?.getItemWeights(
                        auth = "Bearer $token",
                        date1 = date,
                        itemName = dataset[adapterPosition].itemName
                    )?.enqueue(object : Callback<StockWeightTodayResponse?> {
                        override fun onResponse(
                            call: Call<StockWeightTodayResponse?>?,
                            response: Response<StockWeightTodayResponse?>?
                        ) {
                            val body = response?.body()
                            if (body?.message?.data?.isNotEmpty() == true) {
                                findViewById<TextView>(R.id.tvInWeight).text = "Masuk ${body?.message?.data?.get(0)?.stockIn ?: 0}Kg"
                                findViewById<TextView>(R.id.tvOutWeight).text = "Keluar ${body?.message?.data?.get(0)?.stockOut?: 0}Kg"
                            } else {
                                findViewById<TextView>(R.id.tvInWeight).text = "Masuk 0 Kg"
                                findViewById<TextView>(R.id.tvOutWeight).text = "Keluar 0 Kg"
                            }
                        }

                        override fun onFailure(call: Call<StockWeightTodayResponse?>?, t: Throwable?) {

                        }
                    })

            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.stock_list, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.load()
    }

    override fun getItemCount(): Int {
        return  dataset.size
    }
}