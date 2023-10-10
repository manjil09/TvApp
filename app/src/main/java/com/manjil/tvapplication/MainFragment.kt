package com.manjil.tvapplication

import android.content.Intent
import android.os.Bundle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import com.manjil.tvapplication.detailsPage.DetailsActivity
import com.manjil.tvapplication.model.Movie
import com.manjil.tvapplication.searchPage.SearchActivity

class MainFragment : BrowseSupportFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUIElements()

        loadItems()

        setUpEventListeners()
        setOnSearchClickedListener {
            val intent = Intent(context,SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadItems() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        val headerItem = HeaderItem(0, "ItemPresenter")
        val headerItem1 = HeaderItem(1, "Second Header")
        val rowItemAdapter = ArrayObjectAdapter(ItemPresenter())
        rowItemAdapter.add("ITEM 1")
        rowItemAdapter.add("ITEM 2")
        rowItemAdapter.add("ITEM 3")


        val cardItemAdapter = ArrayObjectAdapter(CardPresenter())
        cardItemAdapter.add(
            Movie(
                "First Title",
                "Description for first title",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg"
            )
        )
        cardItemAdapter.add(
            Movie(
                "Second Title",
                "Description for second title",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg"
            )
        )
        cardItemAdapter.add(
            Movie(
                "Third Title",
                "Description for third title",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg"
            )
        )

        rowsAdapter.add(ListRow(headerItem, rowItemAdapter))
        rowsAdapter.add(ListRow(headerItem1, cardItemAdapter))
        adapter = rowsAdapter
    }

    private fun setupUIElements() {
        title = "Android TV"
//        badgeDrawable = AppCompatResources.getDrawable(requireActivity(),R.drawable.badge)

        headersState = HEADERS_HIDDEN
        isHeadersTransitionOnBackEnabled = true


        brandColor = resources.getColor(R.color.fastlane_background, requireActivity().theme)
    }

    private fun setUpEventListeners() {
        onItemViewSelectedListener = ItemViewSelectedListener()
        onItemViewClickedListener =
            OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
                /**
                 * Called when an item inside a row gets clicked.
                 * @param itemViewHolder The view holder of the item that is clicked.
                 * @param item The item that is currently selected.
                 * @param rowViewHolder The view holder of the row which the clicked item belongs to.
                 * @param row The row which the clicked item belongs to.
                 */
                if (itemViewHolder.view is ImageCardView) {
                    val intent = Intent(context, DetailsActivity::class.java)
                    intent.putExtra("movie", item as Movie)
                    startActivity(intent)
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