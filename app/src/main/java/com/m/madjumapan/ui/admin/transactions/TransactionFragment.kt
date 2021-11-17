package com.m.madjumapan.ui.admin.transactions

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.m.madjumapan.*
import com.m.madjumapan.databinding.FragmentTransactionBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class TransactionFragment : Fragment() {

    private var dateFilterType: String = "between"
    private var secondDate: Long? = null
    private var firstDate: Long? = null
    private var status: String? = null
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private val TAG = "Transaction Fragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDatePicker()
        setFilterPickeer()
        status = null
    }

    private fun setFilterPickeer() {
        val items = listOf("Semua", "Masuk", "Keluar")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
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
            dateRangePicker.show(childFragmentManager, "Date")
        }
    }

    private fun getTransactions() {
        val token = SharePreferencesClient.sharePreferences(requireContext())
            ?.getString(SharePreferencesClient.PERSONAL_TOKEN, "invalid token")

        val date1 = DateFormat.format("yyyy-MM-dd", Date(firstDate ?: 0)).toString()
        val date2 = DateFormat.format("yyyy-MM-dd", Date(secondDate ?: 0)).toString()
        if (date1 == date2)  dateFilterType = "one_day"
        else dateFilterType = "between"
        RetrofitClient.retrofitClient()?.create(MadjuMapanApi::class.java)
            ?.getTransactions(
                auth = "Bearer $token",
                date1 = date1,
                date2 = date2,
                status = status,
                dateFilterType = dateFilterType,
                perPersonType = null,
                perPersonId = null
            )?.enqueue(object : Callback<TransactionsResponse?> {
                override fun onResponse(
                    call: Call<TransactionsResponse?>?,
                    response: Response<TransactionsResponse?>?
                ) {
                    if (response?.isSuccessful ==  true) {
                        val body = response.body()
                        binding.apply {
                            rvTransactions.adapter =  TransactionsRv(body?.message?.data as ArrayList<TransactionsResponse.Data>)
                            rvTransactions.layoutManager = LinearLayoutManager(requireContext())
                        }
                    }
                }

                override fun onFailure(call: Call<TransactionsResponse?>?, t: Throwable?) {
                    requireContext().showToast(t?.message.toString())
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}