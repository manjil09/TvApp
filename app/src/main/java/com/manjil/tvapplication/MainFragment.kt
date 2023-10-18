package com.manjil.tvapplication

import android.content.Intent
import android.os.Bundle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.PresenterSelector
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
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

class MainFragment : BrowseSupportFragment() {
    private val movieRepo = MovieRepo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUIElements()
        loadItems()
        setUpEventListeners()
        setOnSearchClickedListener {
            val intent = Intent(context, SearchActivity::class.java)
            startActivity(intent)
        }
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
        cardItemAdapter.addAll(0,movieRepo.getMovieList())
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

        setHeaderPresenterSelector(object : PresenterSelector(){
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
                    } else if (item == "GuidedStep Fragment"){
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

        }
    }
}