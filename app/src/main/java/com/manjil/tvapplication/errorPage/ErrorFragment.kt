package com.manjil.tvapplication.errorPage

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.leanback.app.ErrorSupportFragment
import com.manjil.tvapplication.R

class ErrorFragment : ErrorSupportFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = resources.getString(R.string.app_name)
        setErrorContent();
    }

    private fun setErrorContent() {
        imageDrawable = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_network_error,
            requireActivity().theme
        )
        message = "An error occurred"
        setDefaultBackground(true)

        buttonText = "Dismiss"
        buttonClickListener = View.OnClickListener {
            requireActivity().finish()
        }
    }
}