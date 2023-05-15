package com.jocoo.swork.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.allen.library.SuperTextView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.SpanUtils
import com.bumptech.glide.Glide
import com.gdmm.core.BaseApplication
import com.gdmm.core.network.SessionManager
import com.google.android.material.card.MaterialCardView
import com.jocoo.swork.R
import com.jocoo.swork.bean.TicketDetailInfo
import com.jocoo.swork.data.enum.WorkType
import com.jocoo.swork.ui.login.LoginActivity
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.util.SmartGlideImageLoader
import java.io.ByteArrayOutputStream

fun Context.reLogin() {
    startActivity(Intent(this, LoginActivity::class.java).also {
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    })
    val sm = SessionManager.getInstance(BaseApplication.applicationContext())
    sm.clearAll()
}

fun hasGas(workTypeId: Int): Boolean {
    return when (workTypeId) {
        WorkType.Fire_Id, WorkType.LimitSpace_Id, WorkType.Electric_Id -> {
            true
        }

        else -> false
    }
}

fun getTicketTitle(workTypeId: Int): String {
    return when (workTypeId) {
        WorkType.Fire_Id -> "动火安全作业票"
        WorkType.LimitSpace_Id -> "受限空间安全作业票"
        WorkType.PullingBlocking_Id -> "盲板抽堵安全作业票"
        WorkType.Height_Id -> "高处安全作业票"
        WorkType.Lifting_Id -> "吊装安全作业票"
        WorkType.Electric_Id -> "临时用电安全作业票"
        WorkType.Construction_Id -> "动土安全作业票"
        WorkType.Road_Id -> "断路安全作业票"
        else -> ""
    }
}

fun fillBaseInfo(workTypeId: Int, ll: LinearLayout, info: TicketDetailInfo?) {
    if (info == null) return
    when (workTypeId) {
        WorkType.Fire_Id -> fillFireBaseInfo(ll, info)
        WorkType.LimitSpace_Id -> fillLimitSpaceBaseInfo(ll, info)
        WorkType.PullingBlocking_Id -> fillPullingBlockingBaseInfo(ll, info)
        WorkType.Height_Id -> fillHeightBaseInfo(ll, info)
        WorkType.Lifting_Id -> fillLiftingBaseInfo(ll, info)
        WorkType.Electric_Id -> fillElectricBaseInfo(ll, info)
        WorkType.Construction_Id -> fillConstructionBaseInfo(ll, info)
        WorkType.Road_Id -> fillRoadBaseInfo(ll, info)
    }
}

fun fillRoadBaseInfo(ll: LinearLayout, info: TicketDetailInfo) {
    makeCard(ll) {
        make2Col(it, "企业名称", info.org_name)
        make2Col(it, "作业申请单位", info.department_name)
        make2Col(it, "作业申请时间", info.apply_time)
        make2Col(it, "作业单位", info.work_unit_name)
        make2Col(it, "作业负责人", info.charge_operator_name)
        make2Col(it, "监护人", info.guardian)
        make2Col(it, "作业内容", info.content)
        make2Col(it, "编号", info.no)
        make2Col(it, "作业地点", info.place)
    }
    if (!info.special_content_list.isNullOrEmpty()) {
        makeCard(ll) {
            info.special_content_list.forEach { it1 ->
                if (it1.name == "简图") {
                    make2Col(
                        it,
                        it1.name,
                        "",
                        "https://t7.baidu.com/it/u=1797337163,4088130314&fm=193&f=GIF"/*it1.value*/
                    )
                } else {
                    make2Col(it, it1.name, it1.value)
                }
            }
        }
    }
    if (!info.workers.isNullOrEmpty()) {
        makeCard(ll) {
            makeHeader(it, "作业人", "", "所属单位")
            info.workers.forEach { it1 ->
                make2Col(it, it1.name, it1.worker_type)
            }
        }
    }
    if (!info.relevance_tickets.isNullOrEmpty()) {
        makeCard(ll) {
            makeHeader(it, "关联作业类型", "", "作业票编号")
            info.relevance_tickets.forEach { it1 ->
                make2Col(it, it1.type_name, it1.no)
            }
        }
    }
    makeCard(ll) {
        make2Col(it, "风险辨识结果", info.risk_res)
        make2Col(it, "审核部门", info.auditDepartmentStr)
        make2Col(it, "作业实施时间", "自 ${info.start_time}\n至 ${info.end_time}")
    }
}

fun fillConstructionBaseInfo(ll: LinearLayout, info: TicketDetailInfo) {
    makeCard(ll) {
        make2Col(it, "企业名称", info.org_name)
        make2Col(it, "作业申请单位", info.department_name)
        make2Col(it, "作业申请时间", info.apply_time)
        make2Col(it, "作业单位", info.work_unit_name)
        make2Col(it, "作业负责人", info.charge_operator_name)
        make2Col(it, "监护人", info.guardian)
        make2Col(it, "作业内容", info.content)
        make2Col(it, "编号", info.no)
        make2Col(it, "作业地点", info.place)
    }
    if (!info.special_content_list.isNullOrEmpty()) {
        makeCard(ll) {
            info.special_content_list.forEach { it1 ->
                if (it1.name == "简图") {
                    make2Col(it, it1.name, "", it1.value)
                } else {
                    make2Col(it, it1.name, it1.value)
                }
            }
        }
    }
    if (!info.workers.isNullOrEmpty()) {
        makeCard(ll) {
            makeHeader(it, "作业人", "", "所属单位")
            info.workers.forEach { it1 ->
                make2Col(it, it1.name, it1.worker_type)
            }
        }
    }
    if (!info.relevance_tickets.isNullOrEmpty()) {
        makeCard(ll) {
            makeHeader(it, "关联作业类型", "", "作业票编号")
            info.relevance_tickets.forEach { it1 ->
                make2Col(it, it1.type_name, it1.no)
            }
        }
    }
    makeCard(ll) {
        make2Col(it, "风险辨识结果", info.risk_res)
        make2Col(it, "审核部门", info.auditDepartmentStr)
        make2Col(it, "作业实施时间", "自 ${info.start_time}\n至 ${info.end_time}")
    }
}

fun fillElectricBaseInfo(ll: LinearLayout, info: TicketDetailInfo) {
    makeCard(ll) {
        make2Col(it, "企业名称", info.org_name)
        make2Col(it, "作业申请单位", info.department_name)
        make2Col(it, "作业申请时间", info.apply_time)
        make2Col(it, "作业单位", info.work_unit_name)
        make2Col(it, "作业负责人", info.charge_operator_name)
        make2Col(it, "负责人证书类型", info.charge_operator_name)
        make2Col(it, "负责人证书编号", info.charge_operator_name)
        make2Col(it, "监护人", info.guardian)
        make2Col(it, "作业内容", info.content)
        make2Col(it, "编号", info.no)
        make2Col(it, "作业地点", info.place)
    }
    if (!info.special_content_list.isNullOrEmpty()) {
        makeCard(ll) {
            info.special_content_list.forEach { it1 ->
                make2Col(it, it1.name, it1.value)
            }
        }
    }
    if (!info.certs.isNullOrEmpty()) {
        makeCard(ll) {
            makeHeader(it, "作业人", "证书编号", "证书类型")
            info.certs.forEach { it1 ->
                make3Col(it, it1.name, it1.cert_no, it1.cert_type)
            }
        }
    }
    if (!info.relevance_tickets.isNullOrEmpty()) {
        makeCard(ll) {
            makeHeader(it, "关联作业类型", "", "作业票编号")
            info.relevance_tickets.forEach { it1 ->
                make2Col(it, it1.type_name, it1.no)
            }
        }
    }
    makeCard(ll) {
        make2Col(it, "风险辨识结果", info.risk_res)
        make2Col(it, "审核部门", info.auditDepartmentStr)
        make2Col(
            it, "数据采集类型", when (info.data_get_type) {
                "1" -> {
                    "自动采集"
                }

                "2" -> {
                    "手动填写"
                }

                else -> {
                    ""
                }
            }
        )
        make2Col(it, "作业实施时间", "自 ${info.start_time}\n至 ${info.end_time}")
    }
}

fun fillLiftingBaseInfo(ll: LinearLayout, info: TicketDetailInfo) {
    makeCard(ll) {
        make2Col(it, "企业名称", info.org_name)
        make2Col(it, "作业申请单位", info.department_name)
        make2Col(it, "作业申请时间", info.apply_time)
        make2Col(it, "作业单位", info.work_unit_name)
        make2Col(it, "作业负责人", info.charge_operator_name)
        make2Col(it, "监护人", info.guardian)
        make2Col(it, "编号", info.no)
    }
    if (!info.special_content_list.isNullOrEmpty()) {
        makeCard(ll) {
            info.special_content_list.forEach { it1 ->
                make2Col(it, it1.name, it1.value)
            }
        }
    }
    if (!info.workers.isNullOrEmpty()) {
        makeCard(ll) {
            makeHeader(it, "作业人", "", "所属单位")
            info.workers.forEach { it1 ->
                make2Col(it, it1.name, it1.worker_type)
            }
        }
    }
    if (!info.relevance_tickets.isNullOrEmpty()) {
        makeCard(ll) {
            makeHeader(it, "关联作业类型", "", "作业票编号")
            info.relevance_tickets.forEach { it1 ->
                make2Col(it, it1.type_name, it1.no)
            }
        }
    }
    makeCard(ll) {
        make2Col(it, "风险辨识结果", info.risk_res)
        make2Col(it, "审核部门", info.auditDepartmentStr)
        make2Col(it, "作业实施时间", "自 ${info.start_time}\n至 ${info.end_time}")
    }
}

fun fillHeightBaseInfo(ll: LinearLayout, info: TicketDetailInfo) {
    makeCard(ll) {
        make2Col(it, "企业名称", info.org_name)
        make2Col(it, "作业申请单位", info.department_name)
        make2Col(it, "作业申请时间", info.apply_time)
        make2Col(it, "作业单位", info.work_unit_name)
        make2Col(it, "作业负责人", info.charge_operator_name)
        make2Col(it, "监护人", info.guardian)
        make2Col(it, "作业内容", info.content)
        make2Col(it, "编号", info.no)
    }
    if (!info.special_content_list.isNullOrEmpty()) {
        makeCard(ll) {
            info.special_content_list.forEach { it1 ->
                make2Col(it, it1.name, it1.value)
            }
        }
    }
    if (!info.workers.isNullOrEmpty()) {
        makeCard(ll) {
            makeHeader(it, "作业人", "", "所属单位")
            info.workers.forEach { it1 ->
                make2Col(it, it1.name, it1.worker_type)
            }
        }
    }
    if (!info.relevance_tickets.isNullOrEmpty()) {
        makeCard(ll) {
            makeHeader(it, "关联作业类型", "", "作业票编号")
            info.relevance_tickets.forEach { it1 ->
                make2Col(it, it1.type_name, it1.no)
            }
        }
    }
    makeCard(ll) {
        make2Col(it, "风险辨识结果", info.risk_res)
        make2Col(it, "审核部门", info.auditDepartmentStr)
        make2Col(it, "作业实施时间", "自 ${info.start_time}\n至 ${info.end_time}")
    }
}

fun fillPullingBlockingBaseInfo(ll: LinearLayout, info: TicketDetailInfo) {
    makeCard(ll) {
        make2Col(it, "企业名称", info.org_name)
        make2Col(it, "作业申请单位", info.department_name)
        make2Col(it, "作业申请时间", info.apply_time)
        make2Col(it, "作业单位", info.work_unit_name)
        make2Col(it, "作业负责人", info.charge_operator_name)
        make2Col(it, "监护人", info.guardian)
        make2Col(it, "作业内容", info.content)
        make2Col(it, "编号", info.no)
        make2Col(it, "设备管道名称", info.place)
    }
    makeCard(ll) {
        makeHeader(it, "管道参数", "盲板参数", "操作人")
        var l = SpanUtils().appendLine("介质").setBold()
            .append(info.special_content?.medium ?: "")
            .create()
        var c = SpanUtils().appendLine("材质").setBold()
            .append(info.special_content?.material ?: "")
            .create()
        var r = SpanUtils().appendLine("编制人").setBold()
            .append(info.special_content?.author_name ?: "")
            .create()
        make3Col(it, l.toString(), c.toString(), r.toString())
        l = SpanUtils().appendLine("温度").setBold()
            .append(info.special_content?.temperature ?: "")
            .create()
        c = SpanUtils().appendLine("规格").setBold()
            .append(info.special_content?.specifications ?: "")
            .create()
        r = SpanUtils().appendLine("时间").setBold()
            .append(info.special_content?.author_time ?: "")
            .create()
        make3Col(it, l.toString(), c.toString(), r.toString())
        l = SpanUtils().appendLine("压力").setBold()
            .append(info.special_content?.pressure ?: "")
            .create()
        c = SpanUtils().appendLine("编号").setBold()
            .append(info.special_content?.number ?: "")
            .create()
        make3Col(it, l.toString(), c.toString(), "")
        make2Col(it, "盲板位置图", "", info.special_content?.blueprint)
    }
    if (!info.workers.isNullOrEmpty()) {
        makeCard(ll) {
            makeHeader(it, "作业人", "", "所属单位")
            info.workers.forEach { it1 ->
                make2Col(it, it1.name, it1.worker_type)
            }
        }
    }
    if (!info.relevance_tickets.isNullOrEmpty()) {
        makeCard(ll) {
            makeHeader(it, "关联作业类型", "", "作业票编号")
            info.relevance_tickets.forEach { it1 ->
                make2Col(it, it1.type_name, it1.no)
            }
        }
    }
    makeCard(ll) {
        make2Col(it, "风险辨识结果", info.risk_res)
        make2Col(it, "审核部门", info.auditDepartmentStr)
        make2Col(it, "作业实施时间", "自 ${info.start_time}\n至 ${info.end_time}")
    }
}

fun fillLimitSpaceBaseInfo(ll: LinearLayout, info: TicketDetailInfo) {
    makeCard(ll) {
        make2Col(it, "企业名称", info.org_name)
        make2Col(it, "作业申请单位", info.department_name)
        make2Col(it, "作业申请时间", info.apply_time)
        make2Col(it, "作业单位", info.work_unit_name)
        make2Col(it, "作业负责人", info.charge_operator_name)
        make2Col(it, "监护人", info.guardian)
        make2Col(it, "作业内容", info.content)
        make2Col(it, "编号", info.no)
    }
    if (!info.special_content_list.isNullOrEmpty()) {
        makeCard(ll) {
            info.special_content_list.forEach { it1 ->
                make2Col(it, it1.name, it1.value)
            }
        }
    }
    if (!info.workers.isNullOrEmpty()) {
        makeCard(ll) {
            makeHeader(it, "作业人", "", "所属单位")
            info.workers.forEach { it1 ->
                make2Col(it, it1.name, it1.worker_type)
            }
        }
    }
    if (!info.relevance_tickets.isNullOrEmpty()) {
        makeCard(ll) {
            makeHeader(it, "关联作业类型", "", "作业票编号")
            info.relevance_tickets.forEach { it1 ->
                make2Col(it, it1.type_name, it1.no)
            }
        }
    }
    makeCard(ll) {
        make2Col(it, "风险辨识结果", info.risk_res)
        make2Col(it, "审核部门", info.auditDepartmentStr)
        make2Col(
            it, "数据采集类型", when (info.data_get_type) {
                "1" -> {
                    "自动采集"
                }

                "2" -> {
                    "手动填写"
                }

                else -> {
                    ""
                }
            }
        )
        make2Col(it, "作业实施时间", "自 ${info.start_time}\n至 ${info.end_time}")
    }
}

fun fillFireBaseInfo(ll: LinearLayout, info: TicketDetailInfo) {
    makeCard(ll) {
        make2Col(it, "企业名称", info.org_name)
        make2Col(it, "作业申请单位", info.department_name)
        make2Col(it, "作业申请时间", info.apply_time)
        make2Col(it, "作业单位", info.work_unit_name)
        make2Col(it, "作业负责人", info.charge_operator_name)
        make2Col(it, "监护人", info.guardian)
        make2Col(it, "作业内容", info.content)
        make2Col(it, "编号", info.no)
    }
    makeCard(ll) {
        make2Col(it, "动火地点", info.place)
        info.special_content_list?.forEach { it1 ->
            make2Col(it, it1.name, it1.value)
        }
    }
    if (!info.certs.isNullOrEmpty()) {
        makeCard(ll) {
            makeHeader(it, "动火人", "证书编号", "证书类型")
            info.certs.forEach { it1 ->
                make3Col(it, it1.name, it1.cert_no, it1.cert_type)
            }
        }
    }
    if (!info.relevance_tickets.isNullOrEmpty()) {
        makeCard(ll) {
            makeHeader(it, "关联作业类型", "", "作业票编号")
            info.relevance_tickets.forEach { it1 ->
                make2Col(it, it1.type_name, it1.no)
            }
        }
    }

    makeCard(ll) {
        make2Col(it, "风险辨识结果", info.risk_res)
        make2Col(it, "审核部门", info.auditDepartmentStr)
        make2Col(
            it, "数据采集类型", when (info.data_get_type) {
                "1" -> {
                    "自动采集"
                }

                "2" -> {
                    "手动填写"
                }

                else -> {
                    ""
                }
            }
        )
        make2Col(it, "作业实施时间", "自 ${info.start_time}\n至 ${info.end_time}")
    }
}

fun makeCard(parent: ViewGroup, action: (LinearLayoutCompat) -> Unit) {
    MaterialCardView(parent.context).apply {
        radius = SizeUtils.dp2px(6.0f).toFloat()
        setCardBackgroundColor(Color.WHITE)
        cardElevation = 0.0f
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val llc = LinearLayoutCompat(parent.context)
        llc.orientation = LinearLayoutCompat.VERTICAL
        action.invoke(llc)
        llc.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addView(llc)
        parent.addView(this)
    }
}

fun make2Col(parent: ViewGroup, str1: String?, str2: String?, str3: String? = "") {
    SuperTextView(parent.context).apply {
        setBackgroundResource(R.color.white)
        setLeftTextColor(ContextCompat.getColor(parent.context, R.color.textColorPrimary))
        setLeftString(str1)
        setRightTextColor(ContextCompat.getColor(parent.context, R.color.textColorSecondary))
        setRightString(str2)
        rightTextView.width = SizeUtils.dp2px(200.0f)
        rightTextView.gravity = Gravity.END or Gravity.CENTER_VERTICAL
        minimumHeight = SizeUtils.dp2px(44.0f)
        setPadding(0, SizeUtils.dp2px(8.0f), 0, SizeUtils.dp2px(6.0f))
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        if (!str3.isNullOrEmpty()) {
            Glide.with(parent.context).load(str3).placeholder(R.drawable.app_ic_image_placeholder)
                .override(SizeUtils.dp2px(100.0f), SizeUtils.dp2px(100.0f))
                .into(rightIconIV)
            rightIconIV.setOnClickListener {
                XPopup.Builder(context).asImageViewer(rightIconIV, str3, SmartGlideImageLoader())
                    .show()
            }
        }
        parent.addView(this)
    }
}

fun makeHeader(
    parent: ViewGroup,
    str1: String,
    str2: String,
    str3: String,
    @ColorRes colorBg: Int = R.color.table_header_grey
) {
    SuperTextView(parent.context).apply {
        setBackgroundResource(colorBg)
        setLeftTextColor(ContextCompat.getColor(parent.context, R.color.textColorPrimary))
        setLeftTextIsBold(true)
        setLeftString(str1)
        setCenterTextColor(ContextCompat.getColor(parent.context, R.color.textColorPrimary))
        setCenterTextIsBold(true)
        setCenterString(str2)
        setRightTextColor(ContextCompat.getColor(parent.context, R.color.textColorPrimary))
        setRightTextIsBold(true)
        setRightString(str3)
        minimumHeight = SizeUtils.dp2px(44.0f)
        setPadding(0, SizeUtils.dp2px(8.0f), 0, SizeUtils.dp2px(6.0f))
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        parent.addView(this)
    }
}

fun make3Col(
    parent: ViewGroup,
    str1: String?,
    str2: String?,
    str3: String?,
    @ColorRes colorBg: Int = R.color.white
) {
    SuperTextView(parent.context).apply {
        setBackgroundResource(colorBg)
        setLeftTextColor(ContextCompat.getColor(parent.context, R.color.textColorSecondary))
        setLeftString(str1)
        setLeftTextGravity(Gravity.START)
        leftTextView.maxWidth = resources.getDimensionPixelOffset(R.dimen.dp_90)
        setCenterTextColor(ContextCompat.getColor(parent.context, R.color.textColorSecondary))
        setCenterString(str2)
        setCenterTextGravity(Gravity.CENTER)
        centerTextView.maxWidth = resources.getDimensionPixelOffset(R.dimen.dp_90)
        setRightTextColor(ContextCompat.getColor(parent.context, R.color.textColorSecondary))
        setRightString(str3)
        setRightTextGravity(Gravity.END)
        rightTextView.maxWidth = resources.getDimensionPixelOffset(R.dimen.dp_90)
        minimumHeight = SizeUtils.dp2px(44.0f)
        setPadding(0, SizeUtils.dp2px(8.0f), 0, SizeUtils.dp2px(6.0f))
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        parent.addView(this)
    }
}

fun Bitmap.toByteArray(): ByteArray {
    val bos = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, bos)
    return bos.toByteArray()
}