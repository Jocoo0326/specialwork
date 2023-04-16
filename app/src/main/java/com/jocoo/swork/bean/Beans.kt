package com.jocoo.swork.bean

import com.drake.brv.item.ItemExpand


data class StaffGroupItem(
    var name: String? = null
) : ItemExpand {

    override var itemExpand: Boolean = false

    override var itemGroupPosition: Int = 0

    override var itemSublist: List<Any?>? = null
}
