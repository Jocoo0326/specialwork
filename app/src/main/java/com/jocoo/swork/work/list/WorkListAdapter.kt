package com.jocoo.swork.work.list

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jocoo.swork.R
import com.jocoo.swork.bean.WorkInfo
import com.jocoo.swork.data.enum.WorkMode

class WorkListAdapter(
    private val workMode: Int
) : BaseQuickAdapter<WorkInfo, BaseViewHolder>(
    R.layout.work_list_item
), LoadMoreModule {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        setEmptyView(R.layout.layout_empty_view)
    }

    override fun convert(holder: BaseViewHolder, item: WorkInfo) {
        val str = buildString {
            append("部门: ")
            append(item.department_name)
            append("\n")
            append("编号: ")
            append(item.no)
            append("\n")
            append("时间: ")
            append(item.created_time)
            append("\n")
            append("地点: ")
            append(item.place)
            append("\n")
            append("状态: ")
            append(item.rate_name)
        }
        holder.setText(R.id.tv_info, str)
        holder.setText(R.id.btn_audit, if (workMode == WorkMode.Todo_Id) "审\n核" else "查\n看")
    }
}