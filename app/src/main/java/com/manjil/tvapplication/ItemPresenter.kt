package com.manjil.tvapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.RowHeaderPresenter
import com.manjil.tvapplication.databinding.GridItemBinding

class ItemPresenter: Presenter() {

    class ViewHolder(val binding: GridItemBinding): RowHeaderPresenter.ViewHolder(binding.root)

    /**
     * Creates a new [View].
     */
    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val binding = GridItemBinding.inflate(LayoutInflater.from(parent!!.context),parent,false)
        return ViewHolder(binding)
    }

    /**
     * Binds a [View] to an item.
     */
    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder?, item: Any?) {
        val vh = viewHolder as ViewHolder
        vh.binding.tvTitle.text = item as String
    }

    /**
     * Unbinds a [View] from an item. Any expensive references may be
     * released here, and any fields that are not bound for every item should be
     * cleared here.
     */
    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder?) {
        val vh = viewHolder as ViewHolder
        vh.binding.tvTitle.text = null
    }
}