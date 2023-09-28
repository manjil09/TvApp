package com.manjil.tvapplication.detailsPage

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.manjil.tvapplication.R

class DetailsActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        supportFragmentManager.beginTransaction().replace(R.id.detailsFragment, DetailsFragment()).commitNow()
    }
}