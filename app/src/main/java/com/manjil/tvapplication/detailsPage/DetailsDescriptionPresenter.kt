package com.manjil.tvapplication.detailsPage

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import com.manjil.tvapplication.R
import com.manjil.tvapplication.model.Movie

class DetailsDescriptionPresenter: AbstractDetailsDescriptionPresenter() {

    override fun onBindDescription(vh: ViewHolder, item: Any?) {
        val movie = item as Movie

        vh.title.text = movie.title
        vh.body.text = movie.description
    }
}