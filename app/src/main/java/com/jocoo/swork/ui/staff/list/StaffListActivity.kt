package com.jocoo.swork.ui.staff.list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.gdmm.core.BaseCompatActivity
import com.gdmm.core.extensions.simpleActionBar
import com.jocoo.swork.R
import com.jocoo.swork.bean.OperatorInfo
import com.jocoo.swork.data.COMM_KEY_1
import com.jocoo.swork.data.COMM_KEY_2
import com.jocoo.swork.data.COMM_KEY_3
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.databinding.StaffListActivityBinding
import com.jocoo.swork.databinding.StaffListItemBinding
import dagger.hilt.android.AndroidEntryPoint

@Route(path = NavHub.STAFF_LIST)
@AndroidEntryPoint
class StaffListActivity :
    BaseCompatActivity<StaffListActivityBinding, StaffListState, StaffListViewModel>() {

    @JvmField
    @Autowired(name = COMM_KEY_1)
    var name: String = ""

    @JvmField
    @Autowired(name = COMM_KEY_2)
    var type: Int = 0 //0-department 1-contractor

    @JvmField
    @Autowired(name = COMM_KEY_3)
    var id: String = ""

    override val viewModel: StaffListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            simpleActionBar(toolbar)
            recyclerView.linear().divider {
                setColorRes(R.color.divider_line)
            }.setup {
                addType<OperatorInfo>(R.layout.staff_list_item)
                R.id.staffListItem.onClick {
                    val model = getModel<OperatorInfo>()
                    ARouter.getInstance().build(NavHub.STAFF_DETAIL)
                        .withString(COMM_KEY_1, model.name).withInt(COMM_KEY_2, model.is_face!!)
                        .navigation()
                }
                onBind {
                    getBinding<StaffListItemBinding>().tvName.text = getModel<OperatorInfo>().name
                }
            }
            searchView.setOnCloseListener {
                mBinding.recyclerView.models = viewModel.state.value.list
                return@setOnCloseListener false
            }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val listOperators = viewModel.state.value.list
                    mBinding.recyclerView.models = listOperators?.filter {
                        (it.name?.contains(query ?: "") ?: false)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }
        viewModel.fetchOperatorList(type, name, id)
    }

    override fun bindListener() {

    }

    override fun onViewStateChange(state: StaffListState) {
        mBinding.recyclerView.models = state.list
    }

    override fun getViewBinding() = StaffListActivityBinding.inflate(layoutInflater)

}
