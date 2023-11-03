package com.manjil.tvapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.RowHeaderPresenter
import com.bumptech.glide.Glide
import com.manjil.tvapplication.databinding.MovieCardViewBinding
import com.manjil.tvapplication.model.Movie

class MovieCardPresenter : Presenter() {
    class ViewHolder(val binding: MovieCardViewBinding) :
        RowHeaderPresenter.ViewHolder(binding.root) {}

    /**
     * Creates a new [View].
     */
    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val binding =
            MovieCardViewBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
        return ViewHolder(binding)
    }

    /**
     * Binds a [View] to an item.
     */
    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder?, item: Any?) {
        if (viewHolder is ViewHolder) {
            val movie = item as Movie
            Glide.with(viewHolder.view.context)
                .load(movie.imageUrl)
                .centerCrop()
                .into(viewHolder.binding.imageView)
        }
    }

    /**
     * Unbinds a [View] from an item. Any expensive references may be
     * released here, and any fields that are not bound for every item should be
     * cleared here.
     */
    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder?) {
        if (viewHolder is ViewHolder)
            Glide.with(viewHolder.view.context).clear(viewHolder.binding.imageView)
    }
}