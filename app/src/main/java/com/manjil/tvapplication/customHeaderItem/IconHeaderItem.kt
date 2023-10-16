package com.manjil.tvapplication.customHeaderItem

import androidx.leanback.widget.HeaderItem

class IconHeaderItem: HeaderItem{
    companion object{
        const val ICON_NONE = -1
    }

    var iconResId: Int = ICON_NONE
    constructor(name: String) : super(name)
    constructor(id: Long, name: String) : super(id, name)
    constructor(id: Long, name: String, iconResId: Int) : super(id, name) {
        this.iconResId = iconResId
    }
}