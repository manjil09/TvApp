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
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.manjil.tvapplication.customHeaderItem.IconHeaderItem
import com.manjil.tvapplication.customListRow.CustomListRow
import com.manjil.tvapplication.customListRow.CustomListRowPresenter
import com.manjil.tvapplication.detailsPage.DetailsActivity
import com.manjil.tvapplication.errorPage.ErrorActivity
import com.manjil.tvapplication.guidedStep.GuidedStepActivity
import com.manjil.tvapplication.model.Movie
import com.manjil.tvapplication.model.MovieRepo
import java.util.Timer
import java.util.TimerTask


class MainFragment : RowsSupportFragment() {
    private val movieRepo = MovieRepo()
    private val handler = Handler(Looper.myLooper()!!)
    private var backgroundTimer: Timer? = null
    private lateinit var backgroundManager: BackgroundManager
    private var defaultBackground: Drawable? = null
    private lateinit var backgroundUrl: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val contextThemeWrapper =
            ContextThemeWrapper(requireContext(), R.style.Theme_TVApplication)
        val themedInflater = inflater.cloneInContext(contextThemeWrapper)

        return super.onCreateView(themedInflater, container, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadItems()
        setUpEventListeners()
    }

    private fun loadItems() {
        val rowsAdapter = ArrayObjectAdapter(CustomListRowPresenter())
        val cardPresenter = CardPresenter()

        for (i in 0 until 5) {
            val headerItem1 = IconHeaderItem(i.toLong(), "Category ${i + 1}", R.drawable.ic_play)
            val cardItemAdapter = ArrayObjectAdapter(cardPresenter)
            cardItemAdapter.addAll(0, movieRepo.getMovieList())
            val cardItemListRow = CustomListRow(headerItem1, cardItemAdapter)
            rowsAdapter.add(cardItemListRow)
        }

        // setup for the last row
        val headerItem = IconHeaderItem(0, "ItemPresenter", R.drawable.ic_play)
        val textItemAdapter = ArrayObjectAdapter(ItemPresenter())
        textItemAdapter.add("Error Fragment")
        textItemAdapter.add("GuidedStep Fragment")
        textItemAdapter.add("ITEM 3")
        val textItemListRow = CustomListRow(headerItem, textItemAdapter)

        rowsAdapter.add(textItemListRow)
        adapter = rowsAdapter
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
//                backgroundUrl = item.backgroundUrl
//                startBackgroundTimer()
                val overviewFragment =
                    OverviewFragment.newInstance(item.title, item.description, item.backgroundUrl)

                parentFragmentManager.beginTransaction()
                    .replace(R.id.overviewFragment, overviewFragment).commit()
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

    fun getCurrentRow(): Int = verticalGridView.selectedPosition
    private fun getScreenWidthAndHeight(activity: Activity): Array<Int> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            arrayOf(
                windowMetrics.bounds.width() - insets.left - insets.right,
                windowMetrics.bounds.height()
            )
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            arrayOf(displayMetrics.widthPixels, displayMetrics.heightPixels)
        }
    }
}