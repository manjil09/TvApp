package com.manjil.tvapplication

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.manjil.tvapplication.databinding.ActivityMainBinding

class MainActivity : FragmentActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentFragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainBrowseFragment, MainFragment()).commitNow()

//            supportFragmentManager.beginTransaction().replace(R.id.overviewFragment, OverviewFragment.newInstance("","","")).commitNow()
        }
        setupTabLayout()
        currentFragment = supportFragmentManager.findFragmentById(R.id.mainBrowseFragment)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val focusedView = window.decorView.findFocus()
        Log.d("TAG", "onKeyDown: ${focusedView.id}")

        if (binding.tabLayout.getTabAt(0)?.view?.isFocused == true && keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
            return true

        if (currentFragment != null && keyCode == KeyEvent.KEYCODE_DPAD_UP && currentFragment is MainFragment) {
            val mainFragment = currentFragment as MainFragment
            if (mainFragment.getCurrentRow() == 0) setFocusToSelectedTab()
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun setupTabLayout(){
        addNewTab("Search",0)
        addNewTab("For You",1)
        addNewTab("Movies",2)
        addNewTab("Live",3)
        addNewTab("Shows",4)
    }
    private fun addNewTab(title: String, position: Int){
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(title),position)
    }

    private fun setFocusToSelectedTab() {
        val selectedTabPosition = binding.tabLayout.selectedTabPosition
        binding.tabLayout.getTabAt(selectedTabPosition)?.view?.requestFocus()
    }
}