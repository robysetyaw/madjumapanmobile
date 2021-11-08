package com.m.madjumapan.ui.admin.transactions

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.m.madjumapan.MadjuMapanApi
import com.m.madjumapan.RetrofitClient
import com.m.madjumapan.SharePreferencesClient
import com.m.madjumapan.databinding.FragmentTransactionBinding
import com.m.madjumapan.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class TransactionFragment : Fragment() {

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
            val token = SharePreferencesClient.sharePreferences(requireContext())
                ?.getString(SharePreferencesClient.PERSONAL_TOKEN, "invalid token")
            val date1 = DateFormat.format("yyyy-MM-dd", Date(it.first)).toString()
            val date2 = DateFormat.format("yyyy-MM-dd", Date(it.second)).toString()
            RetrofitClient.retrofitClient()?.create(MadjuMapanApi::class.java)
                ?.getTransactions(
                    auth = "Bearer $token",
                    date1 = date1,
                    date2 = date2
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
        binding.btDatePicker.setOnClickListener {
            dateRangePicker.show(childFragmentManager, "Date")
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}