package com.manjil.tvapplication.errorPage

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.manjil.tvapplication.MainFragment
import com.manjil.tvapplication.R

class ErrorActivity : FragmentActivity() {
    private lateinit var errorFragment: ErrorFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testError()
    }

    private fun testError() {
        errorFragment = ErrorFragment()
        supportFragmentManager.beginTransaction().add(R.id.mainBrowseFragment, errorFragment)
            .commit()
    }
}