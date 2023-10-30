package com.manjil.tvapplication.customListRow

import android.view.ViewGroup
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.ListRowView
import androidx.leanback.widget.RowPresenter

class CustomListRowPresenter : ListRowPresenter() {
    override fun onBindRowViewHolder(holder: RowPresenter.ViewHolder?, item: Any?) {
        val numRows = (item as CustomListRow).numRows
        (holder as ViewHolder).gridView.setNumRows(numRows)

        super.onBindRowViewHolder(holder, item)
    }
    override fun initializeRowViewHolder(holder: RowPresenter.ViewHolder?) {
        super.initializeRowViewHolder(holder)

//        shadowEnabled = false
    }
}