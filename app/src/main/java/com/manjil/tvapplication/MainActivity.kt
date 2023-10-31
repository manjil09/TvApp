package com.manjil.tvapplication

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.manjil.tvapplication.databinding.ActivityMainBinding

class MainActivity : FragmentActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainBrowseFragment, MainFragment()).commitNow()

//            supportFragmentManager.beginTransaction().replace(R.id.overviewFragment, OverviewFragment.newInstance("","","")).commitNow()
        }
        var tabLayout:TabLayout
        tabLayout.getc
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val focusedView = window.decorView.findFocus()
        Log.d("TAG", "onKeyDown: ${focusedView.id}")
        return super.onKeyDown(keyCode, event)
    }
}