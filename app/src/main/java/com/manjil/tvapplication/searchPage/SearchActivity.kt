package com.manjil.tvapplication.searchPage

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.manjil.tvapplication.R

class SearchActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().replace(R.id.searchFragment, SearchFragment())
                .commit()
    }
}