package com.jocoo.swork.work.audit.gas

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jocoo.swork.R
import com.jocoo.swork.bean.GasInfo
import com.jocoo.swork.data.enum.WorkType

class AuditGasAdapter(
    private val workType: Int
) : BaseQuickAdapter<GasInfo, BaseViewHolder>(
    R.layout.work_audit_gas_item
) {

    override fun convert(holder: BaseViewHolder, item: GasInfo) {
        when {
            (workType == WorkType.LimitSpace_Id || workType == WorkType.Electric_Id) -> {
                holder.setGone(R.id.tv_gas_group, false)
                holder.setText(R.id.tv_gas_group, "气体分组\n${item.group_name}")
                holder.setGone(R.id.tv_gas_standard, false)
                holder.setText(R.id.tv_gas_standard, "气体标准\n${item.standard}")
                holder.setGone(R.id.tv_gas_location, false)
                holder.setText(R.id.tv_gas_location, "气体部位\n${item.place}")
            }

            else -> {
                holder.setGone(R.id.tv_gas_group, true)
                holder.setGone(R.id.tv_gas_standard, true)
                holder.setGone(R.id.tv_gas_location, true)
            }
        }

        holder.setText(R.id.tv_people, "分析人：${item.analysis_user}")
        holder.setText(R.id.tv_date, "分析时间：${item.analysis_time}")
        holder.setText(R.id.tv_1, "气体名称\n${item.gas_type_name}")
        holder.setText(R.id.tv_2, "气体浓度\n${item.concentration}")
        holder.setText(R.id.tv_3, "气体单位\n${item.unit_name}")
    }
}