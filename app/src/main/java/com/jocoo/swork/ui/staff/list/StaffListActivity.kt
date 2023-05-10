package com.jocoo.swork.ui.staff.list

import android.os.Bundle
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.gdmm.core.BaseCompatActivity
import com.gdmm.core.extensions.simpleActionBar
import com.jocoo.swork.R
import com.jocoo.swork.bean.StaffItem
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.databinding.StaffListActivityBinding
import com.jocoo.swork.databinding.StaffListItemBinding
import dagger.hilt.android.AndroidEntryPoint

@Route(path = NavHub.STAFF_LIST)
@AndroidEntryPoint
class StaffListActivity :
    BaseCompatActivity<StaffListActivityBinding, StaffListState, StaffListViewModel>() {

    @JvmField
    var name: String = ""

    @JvmField
    var type: Int = 0 //0-department 1-contractor

    @JvmField
    var id: String = ""

    override val viewModel: StaffListViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            simpleActionBar(toolbar)
            recyclerView.linear().divider {
                setColorRes(R.color.divider_line)
            }.setup {
                addType<StaffItem>(R.layout.staff_list_item)
                R.id.staffListItem.onClick {
                    ARouter.getInstance().build(NavHub.STAFF_DETAIL).navigation()
                }
                onBind {
                    getBinding<StaffListItemBinding>().tvName.text =
                        getModel<StaffItem>().workerName
                }
            }
        }
        viewModel.fetchOperatorList(type, name, id)
    }

    override fun bindListener() {

    }

    override fun onViewStateChange(state: StaffListState) {

    }

    override fun getViewBinding() = StaffListActivityBinding.inflate(layoutInflater)

}
