package com.jocoo.swork.bean

import com.drake.brv.item.ItemExpand
import com.drake.brv.item.ItemHover


data class StaffGroupItem(
    var name: String? = null
) : ItemExpand, ItemHover {

    override var itemExpand: Boolean = true

    override var itemGroupPosition: Int = 0

    override var itemSublist: List<Any?>? = null

    override var itemHover: Boolean = true
}

data class StaffChildItem(
    var name: String? = null
)

data class StaffItem(
    var workerId: String? = null,
    var workerName: String? = null,
)