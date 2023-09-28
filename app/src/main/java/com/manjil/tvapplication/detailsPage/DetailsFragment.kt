package com.manjil.tvapplication.detailsPage

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.DetailsOverviewRow
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.manjil.tvapplication.R
import com.manjil.tvapplication.model.Movie
import java.io.Serializable

class DetailsFragment : DetailsSupportFragment() {
    private var movie: Movie? = null
    lateinit var presenterSelector: ClassPresenterSelector
    lateinit var mAdapter: ArrayObjectAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = serializable<Movie>("movie")

        presenterSelector = ClassPresenterSelector()
        mAdapter = ArrayObjectAdapter(presenterSelector)
        setupDetailsOverviewRow()
        setupDetailsOverviewRowPresenter()
        adapter = mAdapter
    }

    private fun setupDetailsOverviewRow() {
        val row = DetailsOverviewRow(movie)
        row.imageDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.default_background)
        Glide.with(requireActivity())
            .load(movie!!.imageUrl)
            .centerCrop()
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    row.imageDrawable = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    row.imageDrawable = placeholder
                }
            })

        val actionAdapter = ArrayObjectAdapter()
        actionAdapter.add(
            Action(
                0,
                "Watch Trailer"
            )
        )
        actionAdapter.add(
            Action(
                2,
                "Buy Movie",
                "$15.99"
            )
        )
        row.actionsAdapter = actionAdapter
        mAdapter.add(row)
    }

    private fun setupDetailsOverviewRowPresenter() {
        val detailsPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())

//        detailsPresenter.backgroundColor = ContextCompat.getColor(requireContext(),R.color.fastlane_background)
        presenterSelector.addClassPresenter(DetailsOverviewRow::class.java, detailsPresenter)
    }

    @Suppress("DEPRECATION")
    private inline fun <reified T : Serializable> serializable(key: String): T? = when {
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ->
            requireActivity().intent.getSerializableExtra(key) as T

        else -> {
            requireActivity().intent.getSerializableExtra(key, T::class.java)
        }
    }
}