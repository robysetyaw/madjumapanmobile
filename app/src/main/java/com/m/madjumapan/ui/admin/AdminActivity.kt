package com.m.madjumapan.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.m.madjumapan.R
import com.m.madjumapan.databinding.ActivityAdminBinding
import com.m.madjumapan.ui.admin.stock.StockFragment
import com.m.madjumapan.ui.admin.transactions.TransactionFragment

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initFragment()
    }

    private fun initFragment() {
        binding.apply {

            supportFragmentManager.beginTransaction().replace(R.id.main_container, StockFragment()).commit()

            bottomNavigation.setOnNavigationItemSelectedListener {
                val fragment: Fragment
                val itemId = it.itemId
                Log.d("admin", "Click: ")
                if (itemId == R.id.page_1) {

                    fragment = StockFragment()
                    val f =  supportFragmentManager.findFragmentById(R.id.main_container)
                    if (f !is StockFragment) {
                        val transaction = supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.main_container, fragment).commit()
                    }
                } else if (itemId == R.id.page_2) {
                    fragment = TransactionFragment()
                    val f =  supportFragmentManager.findFragmentById(R.id.main_container)
                    if (f !is TransactionFragment) {
                        val transaction = supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.main_container, fragment).commit()
                    }
                }
             true
            }
        }
    }
}