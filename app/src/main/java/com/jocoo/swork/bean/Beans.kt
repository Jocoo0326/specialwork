package com.jocoo.swork.bean

import android.graphics.Color
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

@JsonClass(generateAdapter = true)
data class HomeRes(
    var rate_type_total: List<HomeItem>? = null
)

@JsonClass(generateAdapter = true)
data class HomeItem(
    var name: String? = null,
    var total: Int? = 0,
    var id: Int? = 0,
    var data: List<WorkTypeInfo>? = null,
    var bgResId: Int? = 0,
    var contentColor: Int? = Color.BLACK
)

@JsonClass(generateAdapter = true)
data class WorkTypeInfo(
    var name: String? = null,
    var type_id: Int? = 0,
    var total: Int? = 0,
    var bgResId: Int? = 0,
)

@JsonClass(generateAdapter = true)
data class WorkInfo(
    var id: String? = null,
    var org_id: String? = null,
    var type_id: Int? = 0,
    var no: String? = null,
    var rate: String? = null,
    var apply_time: String? = null,
    var content: String? = null,
    var place: String? = null,
    var start_time: String? = null,
    var end_time: String? = null,
    var department_id: String? = null,
    var created: String? = null,
    var type_name: String? = null,
    var rate_name: String? = null,
    var department_name: String? = null,
    var created_time: String? = null,
)