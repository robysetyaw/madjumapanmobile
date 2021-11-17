package com.m.madjumapan.ui.gudang

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.m.madjumapan.*
import com.m.madjumapan.databinding.FragmentOutBinding
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OutFragment : Fragment() {
    private  var inputSupplierName: String? = null
    private  var inputItemName: String? = null
    private var _binding: FragmentOutBinding? = null
    private val binding get() = _binding
    private val TAG = "Out Fragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setItemNamePicker()
        setCustomerNamePicker()
        setInsertItem()
    }

    private fun setCustomerNamePicker() {
        binding?.apply {
            val token = SharePreferencesClient.sharePreferences(requireContext())
                ?.getString(SharePreferencesClient.PERSONAL_TOKEN, "invalid token")
            RetrofitClient.retrofitClient()?.create(MadjuMapanApi::class.java)
                ?.getUserName(
                    auth = "Bearer $token",
                    role = "customer"
                )?.enqueue(object : Callback<UserResponse?> {


                    override fun onResponse(
                        call: Call<UserResponse?>?,
                        response: Response<UserResponse?>?
                    ) {
                        if (response?.isSuccessful == true) {
                            drCustomerNames.setAdapter(null)
                            val body = response.body()?.message
                            val data = body?.data
                            val supplierNames = arrayListOf<String>()
                            data?.forEach {
                                supplierNames.add("${it.username}|${it.id}")
                            }
                            val adapter = context?.let {
                                ArrayAdapter(
                                    it,
                                    R.layout.list_item,
                                    supplierNames ?: listOf()
                                )
                            }
                            binding.apply {
                                this?.drCustomerNames?.setAdapter(adapter)
                                this?.drCustomerNames?.setOnItemClickListener { adapterView, view, i, l ->
                                    inputSupplierName = supplierNames[i]
                                }
                            }
                        }else {
                            requireContext().showToast("error")
                        }
                    }

                    override fun onFailure(call: Call<UserResponse?>?, t: Throwable?) {
                        requireContext().showToast(t?.message.toString())
                    }
                })
        }
    }

    private fun setItemNamePicker() {
        binding?.apply {
            val token = SharePreferencesClient.sharePreferences(requireContext())
                ?.getString(SharePreferencesClient.PERSONAL_TOKEN, "invalid token")

            RetrofitClient.retrofitClient()?.create(MadjuMapanApi::class.java)
                ?.getItemNames(
                    auth = "Bearer $token"
                )?.enqueue(object : Callback<ItemNamesResponse?> {
                    override fun onResponse(
                        call: Call<ItemNamesResponse?>?,
                        response: Response<ItemNamesResponse?>?
                    ) {
                        if (response?.isSuccessful == true) {
                            drItemNames.setAdapter(null)
                            val body =response.body()
                            val data = body?.message?.data
                            val itemNames = arrayListOf<String>()
                            data?.forEach { itemNames.add(it.itemName) }

                            val adapter = context?.let {
                                ArrayAdapter(
                                    it,
                                    R.layout.list_item,
                                    itemNames ?: listOf()
                                )
                            }
                            drItemNames.setAdapter(adapter)
                            drItemNames.setOnItemClickListener { adapterView, view, i, l ->
                                inputItemName = itemNames[i] ?: ""
                            }
                        } else {
                            requireContext().showToast("error")
                        }
                    }

                    override fun onFailure(call: Call<ItemNamesResponse?>?, t: Throwable?) {
                        requireContext().showToast(t?.message.toString())
                    }
                })

        }
    }

    private fun setInsertItem() {
        binding?.apply {
            btInsertItem.setOnClickListener {
                btInsertItem.isEnabled = false
                val weight = tlBerat.editText?.text.toString().trim()
                val price = tlPrice.editText?.text.toString().trim()
                if (inputItemName?.isEmpty() == true) requireContext().showToast("input tidak boleh kosong")
                else if (inputSupplierName?.isEmpty() == true) requireContext().showToast("input tidak boleh kosong")
                else if (weight.isEmpty()) requireContext().showToast("input tidak boleh kosong")
                else if (price.isEmpty()) requireContext().showToast("input tidak boleh kosong")
                else {
                    val requestBody: RequestBody = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("item_name", inputItemName?.trim())
                        .addFormDataPart("item_weight", weight)
                        .addFormDataPart("item_price", price)
                        .addFormDataPart("status", "out")
                        .addFormDataPart("customer_id",
                            inputSupplierName?.split("|")?.get(1)?.trim()
                        )
                        .build()
                    val token = SharePreferencesClient.sharePreferences(requireContext())
                        ?.getString(SharePreferencesClient.PERSONAL_TOKEN, "invalid token")
                    RetrofitClient.retrofitClient()?.create(MadjuMapanApi::class.java)
                        ?.insertItem(
                            auth = "Bearer $token",
                            body = requestBody
                        )?.enqueue(object : Callback<LoginResponse?> {
                            override fun onResponse(
                                call: Call<LoginResponse?>?,
                                response: Response<LoginResponse?>?
                            ) {
                                if (response?.isSuccessful == true) {
                                    requireContext().showToast("Barang sudah dikeluarkan")
                                } else {
                                    val jsonObj = JSONObject(response?.errorBody()!!.charStream().readText())
                                    requireContext().showToast(jsonObj.getString("message"))
                                    requireContext().showToast("Error")
                                }
                            }

                            override fun onFailure(call: Call<LoginResponse?>?, t: Throwable?) {
                                requireContext().showToast(t?.message.toString())
                            }
                        })
                }
                tlBerat.editText?.setText("")
                tlPrice.editText?.setText("")
                tlCustomer.editText?.setText("")
                tlItemName.editText?.setText("")
                btInsertItem.isEnabled = true
                inputItemName = ""
                inputSupplierName = ""
                setItemNamePicker()
                setCustomerNamePicker()

            }
        }

    }


}