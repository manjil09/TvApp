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

        val gridItemPresenterHeader = HeaderItem(0, "GridItemPresenter")
        val gridRowAdapter = ArrayObjectAdapter(GridItemPresenter())
        gridRowAdapter.add("ITEM 1")
        gridRowAdapter.add("ITEM 2")
        gridRowAdapter.add("ITEM 3")
        rowsAdapter.add(ListRow(gridItemPresenterHeader, gridRowAdapter))
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