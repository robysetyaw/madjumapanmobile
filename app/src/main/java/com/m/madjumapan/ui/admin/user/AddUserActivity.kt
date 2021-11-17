package com.m.madjumapan.ui.admin.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import com.google.gson.annotations.SerializedName
import com.m.madjumapan.*
import com.m.madjumapan.databinding.AddCustomersBinding
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class InsertUserResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: Boolean
) {

}


class AddUserActivity : AppCompatActivity() {
    private var inputRoleName: String? = null
    private lateinit var binding: AddCustomersBinding
    private val TAG = "AdduserActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddCustomersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setInputForm()
    }

    private fun setInputForm() {
        binding.apply {
            drRoleNames.setAdapter(null)


            val roleNames = listOf<String>(
                "Admin", "Gudang", "Customer", "Supplier"
            )
            val adapter = ArrayAdapter(
                this@AddUserActivity,
                R.layout.list_item,
                roleNames
            )
            drRoleNames.setAdapter(adapter)
            drRoleNames.setOnItemClickListener { adapterView, view, i, l ->
                inputRoleName = roleNames[i] ?: "Admin"
            }

            btInsertUser.setOnClickListener {
                if (tlNamaUserBaru.editText?.text.toString().trim().isEmpty())  {
                    this@AddUserActivity.showToast("input tidak boleh kosong")
                    return@setOnClickListener
                }

                if (tlPassword.editText?.text.toString().trim().isEmpty())  {
                    this@AddUserActivity.showToast("input tidak boleh kosong")
                    return@setOnClickListener
                }

                if (inputRoleName == null)  {
                    this@AddUserActivity.showToast("input tidak boleh kosong")
                    return@setOnClickListener
                }
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("username", tlNamaUserBaru.editText?.text.toString().trim())
                    .addFormDataPart("password", tlPassword.editText?.text.toString().trim())

                if (inputRoleName == "Admin") {
                    requestBody.addFormDataPart("is_admin", "1")
                    requestBody.addFormDataPart("is_gudang", "1")
                }
                else if (inputRoleName == "Gudang") requestBody.addFormDataPart("is_gudang", "1")
                else if (inputRoleName == "Customer") requestBody.addFormDataPart("is_customer", "1")
                else if (inputRoleName == "Supplier") requestBody.addFormDataPart("is_supplier", "1")
                val token = SharePreferencesClient.sharePreferences(this@AddUserActivity)
                    ?.getString(SharePreferencesClient.PERSONAL_TOKEN, "invalid token")
                RetrofitClient.retrofitClient()?.create(MadjuMapanApi::class.java)
                    ?.insertUser(
                        auth = "Bearer $token",
                        body = requestBody.build()
                    )?.enqueue(object : Callback<InsertUserResponse?> {
                        override fun onResponse(
                            call: Call<InsertUserResponse?>?,
                            response: Response<InsertUserResponse?>?
                        ) {
                            if (response?.isSuccessful == true) {
                                this@AddUserActivity.showToast(response.body()?.status.toString())
                                finish()
                            } else {

                            }
                        }

                        override fun onFailure(call: Call<InsertUserResponse?>?, t: Throwable?) {
                            this@AddUserActivity.showToast(t?.message.toString())
                            Log.d(TAG, "onFailure: ${t?.printStackTrace()}")
                        }
                    })

            }
        }
    }
}