package com.manjil.tvapplication

import android.view.ViewGroup
import androidx.leanback.widget.BaseCardView
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.manjil.tvapplication.model.Movie

class CardPresenter: Presenter() {
    /**
     * Creates a new [View].
     */
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val cardView = ImageCardView(parent.context)
        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        return ViewHolder(cardView)
    }

    /**
     * Binds a [View] to an item.
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        val movie = item as Movie
        val cardView = viewHolder.view as ImageCardView

        cardView.titleText = movie.title
        cardView.contentText = movie.description
        cardView.infoVisibility = BaseCardView.CARD_REGION_VISIBLE_SELECTED
        cardView.setMainImageDimensions(400,200)
        Glide.with(viewHolder.view.context)
            .load(movie.imageUrl)
            .centerCrop()
            .into(cardView.mainImageView)
    }

    /**
     * Unbinds a [View] from an item. Any expensive references may be
     * released here, and any fields that are not bound for every item should be
     * cleared here.
     */
    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        cardView.mainImage = null
        cardView.badgeImage = null
    }
}