package com.m.madjumapan.ui.gudang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.gson.annotations.SerializedName
import com.m.madjumapan.*
import com.m.madjumapan.databinding.FragmentInBinding
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


data class ItemNamesResponse(
    val message: Message,
    val status: String
) {
    data class Message(
        val data: List<Data>
    )
    data class Data(
        @SerializedName("item_name")
        val itemName: String
    )
}

data class UserResponse(
    val message: Message,
    val status: String
) {
    data class Message(
        val data: List<Data>
    )
    data class Data(
        @SerializedName("username")
        val username: String,
        @SerializedName("id")
        val id: String,

    )
}


class InFragment : Fragment() {
    private  var inputSupplierName: String? = null
    private  var inputItemName: String? = null
    private var _binding: FragmentInBinding? = null
    private val binding get() = _binding
    private val TAG = "In Fragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setItemNamePicker()
        setSupplierNamePicker()
        setInsertItem()
    }

    private fun setSupplierNamePicker() {
        binding?.apply {
            val token = SharePreferencesClient.sharePreferences(requireContext())
                ?.getString(SharePreferencesClient.PERSONAL_TOKEN, "invalid token")
            RetrofitClient.retrofitClient()?.create(MadjuMapanApi::class.java)
                ?.getUserName(
                    auth = "Bearer $token",
                    role = "supplier"
                )?.enqueue(object : Callback<UserResponse?> {


                    override fun onResponse(
                        call: Call<UserResponse?>?,
                        response: Response<UserResponse?>?
                    ) {
                        if (response?.isSuccessful == true) {
                            drSupplierNames.setAdapter(null)
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
                                this?.drSupplierNames?.setAdapter(adapter)
                                this?.drSupplierNames?.setOnItemClickListener { adapterView, view, i, l ->
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
                        .addFormDataPart("status", "in")
                        .addFormDataPart("supplier_id",
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
                                    requireContext().showToast("Barang sudah diInput")
                                } else {
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
                tlSupplier.editText?.setText("")
                tlItemName.editText?.setText("")
                btInsertItem.isEnabled = true
                inputItemName = ""
                inputSupplierName = ""
                setItemNamePicker()
                setSupplierNamePicker()

            }
        }

    }


}