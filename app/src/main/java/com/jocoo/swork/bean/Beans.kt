package com.jocoo.swork.bean

import android.graphics.Color
import com.drake.brv.item.ItemExpand
import com.drake.brv.item.ItemHover
import com.drake.brv.item.ItemPosition
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
    val relevance_tickets: List<RelevanceTicket>? = null,
    val worker_ids: List<String>? = null,
    val certs: List<CertInfo>? = null,
    val risk_res: String? = null,
    val check_res: String? = null,
    val data_get_type: String? = null,
    val start_time: String? = null,
    val end_time: String? = null,
    val audit_departments: List<AuditDepartment>? = null,
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
    val checkList: List<CheckInfo>? = null,
    val addCheckList: List<CheckInfo>? = null,
    val startEndTime: List<String>? = null,
    val created_time: String? = null,
    val modified_time: String? = null,
    val org_name: String? = null,
    val processOpinions: List<ProcessOpinion>? = null,
    var sensorDataList: List<GasInfo>? = null,
) {
    val auditDepartmentStr: String
        get() {
            return audit_departments?.map { it.name }?.joinToString(separator = ",") ?: ""
        }
}

@JsonClass(generateAdapter = true)
data class CheckInfo(
    val id: Int? = 0,
    val content: String? = null,
    val isHas: String? = null,
    var checkRes: String? = null,
    var sign: String? = null,
    val author: String? = null,
) {
    var isSelected: Boolean = false
    var isConfirmed: Boolean
        get() {
            return checkRes == "1"
        }
        set(value) {
            checkRes = if (value) "1" else "0"
        }

    val isHasStr: String
        get() {
            return if (isHas == "0") "否" else "是"
        }
}

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

@JsonClass(generateAdapter = true)
data class CertInfo(
    var cert_id: String? = null,
    var name: String? = null,
    var cert_no: String? = null,
    var cert_type: String? = null,
)

@JsonClass(generateAdapter = true)
data class RelevanceTicket(
    var type_name: String? = null,
    var no: String? = null,
)

@JsonClass(generateAdapter = true)
data class AuditDepartment(
    var id: String? = null,
    var name: String? = null,
)

@JsonClass(generateAdapter = true)
data class UploadInfo(
    var imageUrl: String? = null,
)

@JsonClass(generateAdapter = true)
data class GasInfo(
    var id: String? = null,
    var ticket_id: String? = null,
    var gas_type_id: String? = null,
    var gas_type_name: String? = null,
    var concentration: String? = null,
    var unit_type: String? = null,
    var unit_name: String? = null,
    var standard: String? = null,
    var group_id: String? = null,
    var group_name: String? = null,
    var place: String? = null,
    var analysis_time: String? = null,
    var analysis_user: String? = null,
    var status: String? = null,
    var created: String? = null,
    var modified: String? = null,
    var created_by: String? = null,
    var modified_by: String? = null
)

@JsonClass(generateAdapter = true)
data class GasTableOptionsInfo(
    var sensor_data_type: List<SensorDataType>? = null,
    var gas_unit_type: List<GasUnitType>? = null,
    var gas_group_type: List<GasUnitType>? = null,
)

@JsonClass(generateAdapter = true)
data class SensorDataType(
    var id: String? = null,
    var type: String? = null,
    var typeName: String? = null,
    var name: String? = null,
) {
    override fun toString(): String {
        return name ?: ""
    }
}

@JsonClass(generateAdapter = true)
data class GasUnitType(
    var id: String? = null,
    var name: String? = null,
) {
    override fun toString(): String {
        return name ?: ""
    }
}

@JsonClass(generateAdapter = true)
data class GasListResp(
    var list: List<GasInfo>? = null,
)

@JsonClass(generateAdapter = true)
data class TicketOptionsResp(
    var signList: List<SignOption>? = null,
    var opinions: List<OpinionOption>? = null,
)

@JsonClass(generateAdapter = true)
data class SignOption(
    var key: String? = null,
    var name: String? = null,
    override var comment: String? = null,
    override var sign: String? = null,
) : SignInfo

@JsonClass(generateAdapter = true)
data class OpinionOption(
    var id: String? = null,
    var name: String? = null,
    var field: String? = null,
    override var comment: String? = null,
    override var sign: String? = null,
) : SignInfo

interface SignInfo {
    var comment: String?
    var sign: String?
}
