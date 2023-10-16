package com.manjil.tvapplication.customHeaderItem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.RowHeaderPresenter
import com.manjil.tvapplication.R

class IconHeaderItemPresenter : RowHeaderPresenter() {
    private var unselectedAlpha: Float = 1f
    override fun onCreateViewHolder(parent: ViewGroup?): Presenter.ViewHolder {
        unselectedAlpha = parent!!.resources.getFraction(
            androidx.leanback.R.fraction.lb_browse_header_unselect_alpha,
            1,
            1
        )
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.icon_header_item, null)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder?, item: Any?) {
        val iconHeaderItem: IconHeaderItem = (item as ListRow).headerItem as IconHeaderItem
        val rootView: View = viewHolder!!.view

        val iconView: ImageView = rootView.findViewById(R.id.ivHeaderIcon)
        val iconResId = iconHeaderItem.iconResId
        if (iconResId != IconHeaderItem.ICON_NONE) {
            val icon = ResourcesCompat.getDrawable(rootView.resources, iconResId, null)
            iconView.setImageDrawable(icon)
        }

        val label: TextView = rootView.findViewById(R.id.tvHeaderLabel)
        label.text = iconHeaderItem.name
    }

    override fun onSelectLevelChanged(holder: ViewHolder?) {
        holder!!.view.alpha = unselectedAlpha + holder.selectLevel * (1.0f - unselectedAlpha)
    }
}