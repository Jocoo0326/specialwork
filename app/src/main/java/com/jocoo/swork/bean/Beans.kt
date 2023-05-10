package com.jocoo.swork.bean

import com.drake.brv.item.ItemExpand
import com.drake.brv.item.ItemHover
import com.gdmm.core.network.UserInfoItem
import com.squareup.moshi.JsonClass


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

@JsonClass(generateAdapter = true)
data class LoginItem(
    var token: String? = null,
    var user_info: UserInfoItem? = null
)

@JsonClass(generateAdapter = true)
class MMVoid