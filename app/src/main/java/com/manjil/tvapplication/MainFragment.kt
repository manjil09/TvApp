package com.manjil.tvapplication

import android.os.Bundle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter


class MainFragment: BrowseSupportFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUIElements()

        loadItems()
    }

    private fun loadItems() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        val headerItem = HeaderItem(0, "ItemPresenter")
        val headerItem1 = HeaderItem(1,"Second Header")
        val rowItemAdapter = ArrayObjectAdapter(ItemPresenter())
        rowItemAdapter.add("ITEM 1")
        rowItemAdapter.add("ITEM 2")
        rowItemAdapter.add("ITEM 3")


        val cardItemAdapter = ArrayObjectAdapter(CardPresenter())
        cardItemAdapter.add(Movie("First Title","Description for first title","https://storage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg"))


        rowsAdapter.add(ListRow(headerItem, rowItemAdapter))
        rowsAdapter.add(ListRow(headerItem1, cardItemAdapter))
        adapter = rowsAdapter
    }

    private fun setupUIElements(){
        title = "Android TV"
//        badgeDrawable = AppCompatResources.getDrawable(requireActivity(),R.drawable.badge)

        headersState = HEADERS_HIDDEN
        isHeadersTransitionOnBackEnabled = true


        brandColor = resources.getColor(R.color.fastlane_background, requireActivity().theme)
    }
}