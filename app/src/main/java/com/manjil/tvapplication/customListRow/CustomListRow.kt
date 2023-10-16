package com.manjil.tvapplication.customListRow

import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ObjectAdapter

class CustomListRow: ListRow {
    var numRows = 1
    constructor(id: Long, header: HeaderItem, adapter: ObjectAdapter) : super(id, header, adapter)
    constructor(header: HeaderItem, adapter: ObjectAdapter): super(header, adapter)
    constructor(adapter: ObjectAdapter): super(adapter)
}