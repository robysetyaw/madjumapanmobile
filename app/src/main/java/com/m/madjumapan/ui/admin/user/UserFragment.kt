package com.m.madjumapan.ui.admin.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.m.madjumapan.*
import com.m.madjumapan.databinding.FragmentUserBinding
import com.m.madjumapan.ui.gudang.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserFragment : Fragment() {


    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding
    private val TAG = "User Fragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBtLogout()
        setRv()
    }

    private fun setRv() {
        binding?.apply {
            val token = SharePreferencesClient.sharePreferences(requireContext())
                ?.getString(SharePreferencesClient.PERSONAL_TOKEN, "invalid token")
            RetrofitClient.retrofitClient()?.create(MadjuMapanApi::class.java)
                ?.getUserName(
                    auth = "Bearer $token",
                    role = "gudang"
                )?.enqueue(object : Callback<UserResponse?> {
                    override fun onResponse(
                        call: Call<UserResponse?>?,
                        response: Response<UserResponse?>?
                    ) {
                        if (response?.isSuccessful  == true) {
                            rvUserGudang.adapter = UserGudangRv(response.body()?.message?.data as ArrayList<UserResponse.Data>)
                            rvUserGudang.layoutManager = LinearLayoutManager(requireContext())
                        } else {
                            requireContext().showToast("error")
                        }
                    }

                    override fun onFailure(call: Call<UserResponse?>?, t: Throwable?) {

                    }
                })

        }
    }


    private fun setBtLogout() {
        binding?.apply {
            btLogout.setOnClickListener {
                if (SharePreferencesClient.logout(requireContext())) requireActivity().finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}