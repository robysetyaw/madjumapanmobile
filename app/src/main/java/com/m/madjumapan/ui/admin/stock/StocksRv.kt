package com.m.madjumapan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName

data class StockResponse(
    val status : String,
    val message: Message
) {
    data class Message(
        @SerializedName("data") var data : List<Stock>
    )
    data class  Stock(
        @SerializedName("item_name") val itemName : String,
        @SerializedName("stock") val stock : String
    )

}


class StocksRv(val dataset: ArrayList<StockResponse.Stock> = arrayListOf()) : RecyclerView.Adapter<StocksRv.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun load() {
            itemView.apply {
                findViewById<TextView>(R.id.tvPetugas).text = dataset[adapterPosition].itemName
                findViewById<TextView>(R.id.tvStock).text = dataset[adapterPosition].stock
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