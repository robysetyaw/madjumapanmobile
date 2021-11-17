package com.m.madjumapan.ui.admin.user.usergudang

import android.os.Bundle
import android.text.format.DateFormat
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.m.madjumapan.*
import com.m.madjumapan.databinding.ActivityDetailUserGudangBinding
import com.m.madjumapan.ui.admin.transactions.TransactionsResponse
import com.m.madjumapan.ui.admin.transactions.TransactionsRv
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DetailUserGudangActivity : AppCompatActivity() {
    private var idGudang: String? = null
    private var dateFilterType: String = "between"
    private var secondDate: Long? = null
    private var firstDate: Long? = null
    private var status: String? = null
    private lateinit var binding: ActivityDetailUserGudangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserGudangBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val extras = intent.extras
        if (extras != null) {
            idGudang = extras.getString("id_gudang")

        }


        getTransactions()
        setFilterPickeer()
        setDatePicker()
    }

    private fun setFilterPickeer() {
        val items = listOf("Semua", "Masuk", "Keluar")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        val btFIlter = (binding.btFilter .editText as? AutoCompleteTextView)
        btFIlter?.setAdapter(adapter)
        btFIlter?.setOnItemClickListener { adapterView, view, i, l ->
            if (i == 0) status = null
            else if(i == 1) status = "in"
            else if (i == 2) status = "out"
            getTransactions()
        }
    }

    private fun setDatePicker() {
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Pilih Tanggal")
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )

                )
                .build()
        dateRangePicker.addOnPositiveButtonClickListener {
            firstDate = it.first
            secondDate = it.second
            getTransactions()
        }

        binding.btDatePicker.setOnClickListener {
            dateRangePicker.show(supportFragmentManager, "Date")
        }
    }

    private fun getTransactions() {
        val token = SharePreferencesClient.sharePreferences(this)
            ?.getString(SharePreferencesClient.PERSONAL_TOKEN, "invalid token")

        val currentTime = System.currentTimeMillis()
        val date1 = DateFormat.format("yyyy-MM-dd", Date(firstDate ?: currentTime)).toString()
        val date2 = DateFormat.format("yyyy-MM-dd", Date(secondDate ?: currentTime)).toString()
        if (date1 == date2)  dateFilterType = "one_day"
        else dateFilterType = "between"
        RetrofitClient.retrofitClient()?.create(MadjuMapanApi::class.java)
            ?.getTransactions(
                auth = "Bearer $token",
                date1 = date1,
                date2 = date2,
                status = status,
                dateFilterType = dateFilterType,
                perPersonType = "gudang",
                perPersonId = idGudang.toString()
            )?.enqueue(object : Callback<TransactionsResponse?> {
                override fun onResponse(
                    call: Call<TransactionsResponse?>?,
                    response: Response<TransactionsResponse?>?
                ) {
                    if (response?.isSuccessful ==  true) {
                        val body = response.body()
                        binding.apply {
                            rvTransactions.adapter =  TransactionsRv(body?.message?.data as ArrayList<TransactionsResponse.Data>)
                            rvTransactions.layoutManager = LinearLayoutManager(this@DetailUserGudangActivity)
                        }

                    } else {
                        showToast("error")
                    }
                }

                override fun onFailure(call: Call<TransactionsResponse?>?, t: Throwable?) {
                    this@DetailUserGudangActivity.showToast(t?.message.toString())
                }
            })
    }
}