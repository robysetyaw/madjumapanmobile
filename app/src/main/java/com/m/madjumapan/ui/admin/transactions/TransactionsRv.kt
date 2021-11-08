package com.m.madjumapan.ui.admin.transactions

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import com.m.madjumapan.R


data class TransactionsResponse(
    @SerializedName("status") var status : String,
    @SerializedName("message") var message : Message
) {
    data class Message (

        @SerializedName("data") var data : List<Data>

    )

    data class Data (

        @SerializedName("id") var id : Int,
        @SerializedName("gudang_id") var gudangId : Int,
        @SerializedName("name_gudang") var nameGudang: String,
        @SerializedName("customer_id") var customerId : String,
        @SerializedName("supplier_id") var supplierId : Int,
        @SerializedName("item_name") var itemName : String,
        @SerializedName("item_weight") var itemWeight : String,
        @SerializedName("item_price") var itemPrice : Int,
        @SerializedName("total price") var totalPrice : String,
        @SerializedName("status") var status : String,
        @SerializedName("created_at") var createdAt : String

    )
}
class TransactionsRv(val dataset: ArrayList<TransactionsResponse.Data>)  : RecyclerView.Adapter<TransactionsRv.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun load() {
            itemView.apply {

                findViewById<TextView>(R.id.statusText).text = "Status"
                findViewById<TextView>(R.id.namaItem).text = "Nama Item"
                findViewById<TextView>(R.id.hargaPerkilo).text = "Harga /KG"
                findViewById<TextView>(R.id.jumlahBerat).text = "Total Kilogram"
                findViewById<TextView>(R.id.totalHarga).text = "Total Harga"
                findViewById<TextView>(R.id.tvKodeTransaksi).text = dataset[adapterPosition].id.toString()
                findViewById<TextView>(R.id.tvItemName).text = dataset[adapterPosition].itemName
                findViewById<TextView>(R.id.tvStatus).text = dataset[adapterPosition].status
                findViewById<TextView>(R.id.tvPetugas).text = dataset[adapterPosition].nameGudang
                findViewById<TextView>(R.id.tvDate).text = dataset[adapterPosition].createdAt
                findViewById<TextView>(R.id.tvWeight).text = dataset[adapterPosition].itemWeight
                findViewById<TextView>(R.id.tvPrice).text = dataset[adapterPosition].itemPrice.toString()
                findViewById<TextView>(R.id.tvTotalPrice).text = dataset[adapterPosition].totalPrice
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_list, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.load()
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}