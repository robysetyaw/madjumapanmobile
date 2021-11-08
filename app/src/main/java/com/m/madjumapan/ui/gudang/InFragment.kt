package com.m.madjumapan.ui.gudang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.m.madjumapan.R
import com.m.madjumapan.databinding.FragmentInBinding


class InFragment : Fragment() {

    private var _binding: FragmentInBinding? = null
    private val binding get() = _binding!!
    private val TAG = "In Fragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val type = arrayOf("Bed-sitter", "Single", "1- Bedroom", "2- Bedroom", "3- Bedroom")
//
//        binding.apply {
//            val type = arrayOf("Bed-sitter", "Single", "1- Bedroom", "2- Bedroom", "3- Bedroom")
//
//            val adapter: ArrayAdapter<String> = ArrayAdapter<Any?>(
//                this,
//                R.layout.stock_list,
//                type
//            )
//            outlinedExposedDropdownEditable.setAdapter(adapter)
//        }
    }


}