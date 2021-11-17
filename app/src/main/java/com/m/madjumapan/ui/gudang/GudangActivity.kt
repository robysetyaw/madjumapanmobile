package com.m.madjumapan.ui.gudang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.m.madjumapan.R
import com.m.madjumapan.databinding.ActivityAdminBinding
import com.m.madjumapan.databinding.ActivityGudangBinding
import com.m.madjumapan.ui.admin.stock.StockFragment
import com.m.madjumapan.ui.admin.transactions.TransactionFragment

class GudangActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGudangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGudangBinding.inflate(layoutInflater)
        val view = binding.root
        
        setContentView(view)

        initFragment()
    }

    private fun initFragment() {
        binding.apply {

            supportFragmentManager.beginTransaction().replace(R.id.main_container, InFragment()).commit()

            bottomNavigation.setOnNavigationItemSelectedListener {
                val fragment: Fragment
                val itemId = it.itemId
                if (itemId == R.id.page_1) {
                    fragment = InFragment()
                    val f =  supportFragmentManager.findFragmentById(R.id.main_container)
                    if (f !is InFragment) {
                        val transaction = supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.main_container, fragment).commit()
                    }
                } else if (itemId == R.id.page_2) {
                    fragment = OutFragment()
                    val f =  supportFragmentManager.findFragmentById(R.id.main_container)
                    if (f !is OutFragment) {
                        val transaction = supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.main_container, fragment).commit()
                    }
                }
                true
            }
        }
    }
}