package com.manjil.tvapplication

import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import com.manjil.tvapplication.customHeaderItem.IconHeaderItem
import com.manjil.tvapplication.customListRow.CustomListRow
import com.manjil.tvapplication.customListRow.CustomListRowPresenter
import com.manjil.tvapplication.detailsPage.DetailsActivity
import com.manjil.tvapplication.errorPage.ErrorActivity
import com.manjil.tvapplication.guidedStep.GuidedStepActivity
import com.manjil.tvapplication.model.Movie
import com.manjil.tvapplication.model.MovieRepo

class MainFragment : RowsSupportFragment() {
    private val movieRepo = MovieRepo()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(requireContext(), R.style.Theme_TVApplication)
        val themedInflater = inflater.cloneInContext(contextThemeWrapper)

        return super.onCreateView(themedInflater, container, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadItems()
        setUpEventListeners()
    }

    private fun loadItems() {
        val presenterSelector = ClassPresenterSelector()
        presenterSelector.addClassPresenter(
            CustomListRow::class.java,
            CustomListRowPresenter().apply { shadowEnabled = false })
        presenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())

        val rowsAdapter = ArrayObjectAdapter(presenterSelector)

        for (i in 0 until 5) {
            val headerItem1 = IconHeaderItem("Category ${i + 1}")
            val cardItemAdapter = ArrayObjectAdapter(MovieCardPresenter())
            cardItemAdapter.addAll(0, movieRepo.getMovieList().shuffled())
            val cardItemListRow = CustomListRow(headerItem1, cardItemAdapter)
            rowsAdapter.add(cardItemListRow)
        }

        // setup for the last row
        val headerItem = IconHeaderItem(0, "ItemPresenter", R.drawable.ic_play)
        val textItemAdapter = ArrayObjectAdapter(ItemPresenter())
        textItemAdapter.add("Error Fragment")
        textItemAdapter.add("GuidedStep Fragment")
        textItemAdapter.add("ITEM 3")
        val textItemListRow = ListRow(headerItem, textItemAdapter)

        rowsAdapter.add(textItemListRow)
        adapter = rowsAdapter
    }

    private fun setUpEventListeners() {
        onItemViewSelectedListener = ItemViewSelectedListener()
        onItemViewClickedListener = OnItemViewClickedListener { _, item, _, row ->
            /**
             * Called when an item inside a row gets clicked.
             * @param <anonymous parameter 0> The view holder of the item that is clicked.
             * @param item The item that is currently selected.
             * @param <anonymous parameter 2> The view holder of the row which the clicked item belongs to.
             * @param row The row which the clicked item belongs to.
             */
            if (item is Movie) {
                val intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra("movie", item)
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
                val overviewFragment =
                    OverviewFragment.newInstance(item.title, item.description, item.backgroundUrl)

                parentFragmentManager.beginTransaction()
                    .replace(R.id.overviewFragment, overviewFragment).commit()
            }
        }
    }

    fun getCurrentRow(): Int = verticalGridView.selectedPosition
}