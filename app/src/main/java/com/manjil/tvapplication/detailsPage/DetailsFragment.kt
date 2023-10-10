package com.manjil.tvapplication.detailsPage

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.DetailsOverviewRow
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnActionClickedListener
import androidx.leanback.widget.OnItemViewClickedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.manjil.tvapplication.CardPresenter
import com.manjil.tvapplication.model.Movie
import com.manjil.tvapplication.playbackPage.VideoPlaybackActivity
import java.io.Serializable

class DetailsFragment : DetailsSupportFragment() {
    private var movie: Movie? = null
    private lateinit var presenterSelector: ClassPresenterSelector
    private lateinit var mAdapter: ArrayObjectAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = serializable<Movie>("movie")

        presenterSelector = ClassPresenterSelector()
        mAdapter = ArrayObjectAdapter(presenterSelector)
        setupDetailsOverviewRow()
        setupRelatedVideoRow()
        setupDetailsOverviewRowPresenter()
        setupEventListeners()
        adapter = mAdapter
    }

    private fun setupEventListeners() {
        onItemViewClickedListener =
            OnItemViewClickedListener { _, item, _, row ->
                /**
                 * Called when an item inside a row gets clicked.
                 * @param <anonymous parameter 0> The view holder of the item that is clicked.
                 * @param item The item that is currently selected.
                 * @param <anonymous parameter 2> The view holder of the row which the clicked item belongs to.
                 * @param row The row which the clicked item belongs to.
                 */
                if (row.id == 0L) {
                    val intent = Intent(requireContext(), DetailsActivity::class.java)
                    intent.putExtra("movie", item as Movie)
                    startActivity(intent)
                }
            }

    }

    private fun setupRelatedVideoRow() {
        val headerItem = HeaderItem(0, "Related Videos")
        val listRowAdapter = ArrayObjectAdapter(CardPresenter())
        listRowAdapter.add(
            Movie(
                "First Title",
                "Description for first title",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg"
            )
        )
        listRowAdapter.add(
            Movie(
                "Second Title",
                "Description for second title",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg"
            )
        )
        listRowAdapter.add(
            Movie(
                "Third Title",
                "Description for third title",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg"
            )
        )
        mAdapter.add(ListRow(headerItem, listRowAdapter))
        presenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())
    }

    private fun setupDetailsOverviewRow() {
        val row = DetailsOverviewRow(movie)
        Glide.with(requireActivity())
            .load(movie!!.imageUrl)
            .centerCrop()
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?,
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
                "Watch Trailer",
                ""
            )
        )
        actionAdapter.add(
            Action(
                1,
                "Buy Movie",
                "$15.99"
            )
        )
        row.actionsAdapter = actionAdapter
        mAdapter.add(row)
    }

    private fun setupDetailsOverviewRowPresenter() {
        val detailsPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())
        detailsPresenter.onActionClickedListener = OnActionClickedListener {
            if (it.id == 0L) {
                val intent = Intent(requireContext(), VideoPlaybackActivity::class.java)
                intent.putExtra("movie", movie)
                startActivity(intent)
            }
        }
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