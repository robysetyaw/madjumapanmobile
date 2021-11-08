package com.m.madjumapan.ui.admin.stock

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.m.madjumapan.*
import com.m.madjumapan.SharePreferencesClient.PERSONAL_TOKEN
import com.m.madjumapan.databinding.FragmentStockBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StockFragment : Fragment() {


    private var _binding: FragmentStockBinding? = null
    private val binding get() = _binding!!
    private val TAG = "Stock Fragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRv()
    }

    private fun setRv() {
        val token = SharePreferencesClient.sharePreferences(requireContext())
            ?.getString(PERSONAL_TOKEN, "invalid token")
        RetrofitClient.retrofitClient()?.create(MadjuMapanApi::class.java)
            ?.getStock(auth = "Bearer $token")?.enqueue(object : Callback<StockResponse?> {
                override fun onResponse(
                    call: Call<StockResponse?>?,
                    response: Response<StockResponse?>?
                ) {
                    if (response?.isSuccessful == true) {
                        val body = response.body()
                        Log.d(TAG, "onResponse: ${response.isSuccessful}")
                        Log.d(TAG, "onResponse: ${response.body()}")
                        binding.apply {
                            rvStock.adapter = StocksRv(body?.message?.data as ArrayList<StockResponse.Stock>)
                            rvStock.layoutManager = GridLayoutManager(requireContext(), 2)
                        }
                    } else {
                        Log.d(TAG, "onResponse: ${response?.isSuccessful}")
                        Log.d(TAG, "onResponse: $response")
                    }
                }

                override fun onFailure(call: Call<StockResponse?>?, t: Throwable?) {
                    Log.d(TAG, "onFailure: $t")
                    context?.showToast(t?.message.toString())
                }

            })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}