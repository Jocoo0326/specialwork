package com.jocoo.swork.widget

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.divider
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.gdmm.core.extensions.observeWithLifecycle
import com.jocoo.swork.R
import com.jocoo.swork.bean.WorkUnitItem
import com.jocoo.swork.databinding.DialogDepartmentTreeChildBinding
import com.jocoo.swork.databinding.DialogDepartmentTreeChildItemBinding
import com.jocoo.swork.ui.staff.StaffViewModel
import com.lxj.xpopup.core.BottomPopupView

class DepartmentTreeDialog(
    context: Context,
    private val viewModel: StaffViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val callback: (List<WorkUnitItem>?) -> Unit
) : BottomPopupView(context) {

    override fun onCreate() {
        super.onCreate()
        val rv = findViewById<RecyclerView>(R.id.rcvParent)
        val cancelBtn = findViewById<View>(R.id.tvCancel)
        val okBtn = findViewById<View>(R.id.tvConfirm)
        cancelBtn.setOnClickListener {
            dismiss()
        }
        okBtn.setOnClickListener {
            val ctl = viewModel.state.value.treeList
            var sw: WorkUnitItem? = null
            if (!ctl.isNullOrEmpty()) {
                for (index in ctl.size - 1 downTo 0) {
                    val elm = ctl[index]
                    val childWorkItem = elm?.firstOrNull { it.isSelected }
                    if (childWorkItem != null) {
                        sw = childWorkItem
                        break
                    }
                }
            }
            callback(
                if (sw == null) {
                    viewModel.state.value.treeList?.firstOrNull()
                } else {
                    listOf(sw)
                }
            )
            dismiss()
        }
        rv.divider {
            setColorRes(R.color.divider_line)
            endVisible = true
        }.setup {
            addType<List<WorkUnitItem>>(R.layout.dialog_department_tree_child)
            onBind {
                val list1 = getModel<List<WorkUnitItem>>()
                getBinding<DialogDepartmentTreeChildBinding>().rcvChild
                    .setup {
                        addType<WorkUnitItem>(R.layout.dialog_department_tree_child_item)
                        onBind {
                            val work = getModel<WorkUnitItem>()
                            getBinding<DialogDepartmentTreeChildItemBinding>().rbtnItem.apply {
                                text = work.name
                                isChecked = work.isSelected
                            }
                        }
                        R.id.rbtnItem.onClick {
                            val work = getModel<WorkUnitItem>()
                            list1.forEach {
                                it.isSelected = work.id == it.id
                            }
                            notifyDataSetChanged()
                            viewModel.fetchDepartmentList2(work.id!!)
                        }
                    }.models = list1
            }
        }
        viewModel.state.observeWithLifecycle(viewLifecycleOwner) {
            rv.models = it.treeList
        }
        viewModel.fetchDepartmentList2()
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_department_tree
    }
}