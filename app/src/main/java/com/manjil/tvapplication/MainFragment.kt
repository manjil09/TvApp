package com.manjil.tvapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Insets
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.WindowInsets
import androidx.core.content.ContextCompat
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.PresenterSelector
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.manjil.tvapplication.customHeaderItem.IconHeaderItem
import com.manjil.tvapplication.customHeaderItem.IconHeaderItemPresenter
import com.manjil.tvapplication.customListRow.CustomListRow
import com.manjil.tvapplication.customListRow.CustomListRowPresenter
import com.manjil.tvapplication.detailsPage.DetailsActivity
import com.manjil.tvapplication.errorPage.ErrorActivity
import com.manjil.tvapplication.guidedStep.GuidedStepActivity
import com.manjil.tvapplication.model.Movie
import com.manjil.tvapplication.model.MovieRepo
import com.manjil.tvapplication.searchPage.SearchActivity
import java.util.Timer
import java.util.TimerTask

class MainFragment : BrowseSupportFragment() {
    private val movieRepo = MovieRepo()
    private val handler = Handler(Looper.myLooper()!!)
    private var backgroundTimer: Timer? = null
    private lateinit var backgroundManager: BackgroundManager
    private var defaultBackground: Drawable? = null
    private lateinit var backgroundUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prepareBackground()
        setupUIElements()
        loadItems()
        setUpEventListeners()
        setOnSearchClickedListener {
            val intent = Intent(context, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun prepareBackground() {
        backgroundManager = BackgroundManager.getInstance(requireActivity())
        backgroundManager.attach(requireActivity().window)
        defaultBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.default_background)
    }

    private fun loadItems() {
        val rowsAdapter = ArrayObjectAdapter(CustomListRowPresenter())

        //setup for the first row
        val headerItem = IconHeaderItem(0, "ItemPresenter", R.drawable.ic_play)
        val textItemAdapter = ArrayObjectAdapter(ItemPresenter())
        textItemAdapter.add("Error Fragment")
        textItemAdapter.add("GuidedStep Fragment")
        textItemAdapter.add("ITEM 3")
        val textItemListRow = CustomListRow(headerItem, textItemAdapter)

        //setup for the second row
        val headerItem1 = IconHeaderItem(1, "Second Header", R.drawable.ic_play)
        val cardItemAdapter = ArrayObjectAdapter(CardPresenter())
        cardItemAdapter.addAll(0, movieRepo.getMovieList())
        val cardItemListRow = CustomListRow(headerItem1, cardItemAdapter)
        cardItemListRow.numRows = 2

        rowsAdapter.add(textItemListRow)
        rowsAdapter.add(cardItemListRow)
        adapter = rowsAdapter
    }

    private fun setupUIElements() {
        title = "Android TV"
//        badgeDrawable = AppCompatResources.getDrawable(requireActivity(),R.drawable.badge)

        headersState = HEADERS_HIDDEN
        isHeadersTransitionOnBackEnabled = true

        setHeaderPresenterSelector(object : PresenterSelector() {
            /**
             * Returns a presenter for the given item.
             */
            override fun getPresenter(item: Any?): Presenter {
                return IconHeaderItemPresenter()
            }

        })

        brandColor = resources.getColor(R.color.fastlane_background, requireActivity().theme)
    }

    private fun setUpEventListeners() {
        onItemViewSelectedListener = ItemViewSelectedListener()
        onItemViewClickedListener =
            OnItemViewClickedListener { itemViewHolder, item, _, row ->
                /**
                 * Called when an item inside a row gets clicked.
                 * @param itemViewHolder The view holder of the item that is clicked.
                 * @param item The item that is currently selected.
                 * @param <anonymous parameter 2> The view holder of the row which the clicked item belongs to.
                 * @param row The row which the clicked item belongs to.
                 */
                if (itemViewHolder.view is ImageCardView) {
                    val intent = Intent(context, DetailsActivity::class.java)
                    intent.putExtra("movie", item as Movie)
                    startActivity(intent)
                } else if (row.headerItem.id == 0L) {
                    if (item == "Error Fragment") {
                        val intent = Intent(context, ErrorActivity::class.java)
                        startActivity(intent)
                    } else if (item == "GuidedStep Fragment") {
                        val intent = Intent(context, GuidedStepActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
    }

    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?,
        ) {
            if (item is Movie) {
                backgroundUrl = item.backgroundUrl
                startBackgroundTimer()
            }
        }
    }

    private fun updateBackground(url: String) {
        val width = getScreenWidthAndHeight(requireActivity())[0]
        val height = getScreenWidthAndHeight(requireActivity())[1]
        Glide.with(requireActivity())
            .load(url)
            .centerCrop()
            .into(object : CustomTarget<Drawable>(width, height) {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?,
                ) {
                    backgroundManager.drawable = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    backgroundManager.drawable = placeholder
                }

            })
    }

    private fun startBackgroundTimer() {
        backgroundTimer?.cancel()
        backgroundTimer = Timer()
        backgroundTimer?.schedule(UpdateBackgroundTask(), 500L)
    }

    private inner class UpdateBackgroundTask : TimerTask() {
        /**
         * The action to be performed by this timer task.
         */
        override fun run() {
            handler.post { updateBackground(backgroundUrl) }
        }

    }

    private fun getScreenWidthAndHeight(activity: Activity): Array<Int> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            arrayOf(windowMetrics.bounds.width() - insets.left - insets.right, windowMetrics.bounds.height())
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            arrayOf(displayMetrics.widthPixels, displayMetrics.heightPixels)
        }
    }
}