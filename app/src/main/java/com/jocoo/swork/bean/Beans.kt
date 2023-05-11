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

@JsonClass(generateAdapter = true)
data class LoginItem(
    var token: String? = null,
    var user_info: UserInfoItem? = null
)

@JsonClass(generateAdapter = true)
class MMVoid

@JsonClass(generateAdapter = true)
data class WorkUnitItem(
    var id: String? = null,
    var name: String? = null,
    var isDepartment: Boolean = false,
    var isSelected: Boolean = false
)

@JsonClass(generateAdapter = true)
data class PageItem<T>(
    var lists: List<T>? = null,
)

@JsonClass(generateAdapter = true)
data class OperatorInfo(
    var id: String? = null,
    var name: String? = null,
    var contractor_id: String? = null,
    var is_face: Int? = 0,
    var contractor_name: String? = null
)