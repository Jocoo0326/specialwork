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

@JsonClass(generateAdapter = true)
data class TicketInfoRes(
    var info: TicketDetailInfo
)

@JsonClass(generateAdapter = true)
data class TicketDetailInfo(
    val id: String? = null,
    val parent_id: String? = null,
    val org_id: String? = null,
    val type_id: Int? = 0,
    val no: String? = null,
    val rate: String? = null,
    val department_id: String? = null,
    val apply_time: String? = null,
    val guardian: String? = null,
    val content: String? = null,
    val place: String? = null,
    val special_content: SpecialContent? = null,
    val work_unit_type: Int? = 0,
    val work_unit_id: Int? = 0,
    val charge_type: Int? = 0,
    val charge_operator_id: Int? = 0,
    val relevance_ticket_types: List<String>? = null,
    val relevance_ticket_ids: List<String>? = null,
    val worker_ids: List<String>? = null,
    val cert_ids: String? = null,
    val risk_res: String? = null,
    val check_res: String? = null,
    val data_get_type: String? = null,
    val start_time: String? = null,
    val end_time: String? = null,
    val audit_department_ids: String? = null,
    val amendment: String? = null,
    val disclosure_sign: String? = null,
    val accept_disclosure_sign: String? = null,
    val author: String? = null,
    val status: String? = null,
    val created: String? = null,
    val modified: String? = null,
    val createdBy: String? = null,
    val modifiedBy: String? = null,
    val department_name: String? = null,
    val work_unit_name: String? = null,
    val charge_operator_name: String? = null,
    val workers: List<Worker>? = null,
    val special_content_list: List<SpecialContentList>? = null,
    val checkList: List<CheckList>? = null,
    val addCheckList: List<AddCheckList>? = null,
    val startEndTime: List<String>? = null,
    val created_time: String? = null,
    val modified_time: String? = null,
    val org_name: String? = null,
    val processOpinions: List<ProcessOpinion>? = null,
)

@JsonClass(generateAdapter = true)
data class AddCheckList(
    val content: String? = null,
    val author: String? = null,
    val id: Int? = 0,
    val isHas: Int? = 0,
    val checkRes: String? = null,
    val sign: String? = null
)

@JsonClass(generateAdapter = true)
data class CheckList(
    val id: String? = null,
    val content: String? = null,
    val isHas: String? = "0",
    val checkRes: String? = null,
    val sign: String? = null
)

@JsonClass(generateAdapter = true)
data class ProcessOpinion(
    val id: Int? = 0,
    val name: String? = null,
    val field: String? = null,
    val content: String? = null,
    val sign: String? = null,
)

@JsonClass(generateAdapter = true)
data class SpecialContent(
    val fire_rank: String? = null,
    val way: String? = null,
    val blueprint: String? = null,
    val medium: String? = null,
    val temperature: String? = null,
    val pressure: String? = null,
    val material: String? = null,
    val specifications: String? = null,
    val number: String? = null,
    val author_name: String? = null,
    val author_time: String? = null,
)

@JsonClass(generateAdapter = true)
data class SpecialContentList(
    val name: String? = null,
    val value: String? = null,
)

@JsonClass(generateAdapter = true)
data class Worker(
    val operatorID: String? = null,
    val name: String? = null,
    val worker_type: String? = null,
)
