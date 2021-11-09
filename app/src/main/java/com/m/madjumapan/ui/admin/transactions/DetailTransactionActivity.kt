package com.m.madjumapan.ui.admin.transactions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.m.madjumapan.R
import com.m.madjumapan.databinding.ActivityDetailTransactionBinding
import com.m.madjumapan.databinding.ActivityDetailUserGudangBinding

class DetailTransactionActivity : AppCompatActivity() {
    private var itemName: String? = null
    private lateinit var binding: ActivityDetailTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTransactionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val extras = intent.extras
        if (extras != null) {
            itemName = extras.getString("id_gudang")
        }
//        i.putExtra("item_name", dataset[adapterPosition].itemName)
//        i.putExtra("name_gudang", dataset[adapterPosition].nameGudang)
//        i.putExtra("item_price", dataset[adapterPosition].itemPrice)
//        i.putExtra("total_item_price", dataset[adapterPosition].totalPrice)
//        i.putExtra("item_weight", dataset[adapterPosition].itemWeight)
//        i.putExtra("created_at", dataset[adapterPosition].createdAt)
//        i.putExtra("status", dataset[adapterPosition].status)
    }


}