package com.m.madjumapan.ui.admin.user

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.m.madjumapan.R
import com.m.madjumapan.ui.admin.user.usergudang.DetailUserGudangActivity
import com.m.madjumapan.ui.gudang.UserResponse


class UserGudangRv(val dataset: ArrayList<UserResponse.Data>):   RecyclerView.Adapter<UserGudangRv.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun load() {
            itemView.apply {
                findViewById<TextView>(R.id.tvUsername).text = "${dataset[adapterPosition].username}"
                setOnClickListener {
                    val value = dataset[adapterPosition].id
                    val i = Intent(context, DetailUserGudangActivity::class.java)
                    i.putExtra("id_gudang", value)
                    startActivity(context, i, null)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_gudang_list, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.load()
    }

    override fun getItemCount(): Int {
     return dataset.size
    }

}