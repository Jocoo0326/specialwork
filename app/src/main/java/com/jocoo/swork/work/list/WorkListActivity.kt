package com.jocoo.swork.work.list

import android.os.Bundle
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.drake.brv.utils.divider
import com.gdmm.core.BaseCompatActivity
import com.gdmm.core.extensions.simpleActionBar
import com.jocoo.swork.bean.WorkInfo
import com.jocoo.swork.data.COMM_KEY_1
import com.jocoo.swork.data.COMM_KEY_2
import com.jocoo.swork.data.COMM_KEY_3
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.data.enum.WorkMode
import com.jocoo.swork.data.enum.WorkType
import com.jocoo.swork.databinding.ActivityWorkListBinding
import com.jocoo.swork.util.PaginationDelegation
import dagger.hilt.android.AndroidEntryPoint

@Route(path = NavHub.WORK_LIST)
@AndroidEntryPoint
class WorkListActivity :
    BaseCompatActivity<ActivityWorkListBinding, WorkListState, WorkListViewModel>() {

    private lateinit var pg: PaginationDelegation<WorkInfo, BaseViewHolder, WorkListAdapter>
    private lateinit var mAdapter: WorkListAdapter
    override val viewModel: WorkListViewModel by viewModels()

    @JvmField
    @Autowired(name = COMM_KEY_1)
    var workMode: Int = WorkMode.Todo_Id

    @JvmField
    @Autowired(name = COMM_KEY_2)
    var typeId: Int = WorkType.Fire_Id

    @JvmField
    @Autowired(name = COMM_KEY_3)
    var typeName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            simpleActionBar(toolbar)
            toolbar.title = "作业票列表$typeName"
            smart.setEnableLoadMore(false)
            smart.setOnRefreshListener {
                viewModel.getTicketList(workMode, typeId)
            }
            mAdapter = WorkListAdapter(workMode)
            recyclerView.divider {
                setDivider(10, true)
                startVisible = true
            }
            recyclerView.adapter = mAdapter
        }
        pg = PaginationDelegation(mAdapter) { page ->
            viewModel.getTicketList(workMode, typeId, page)
        }
    }

    override fun onLoadCompleted() {
        super.onLoadCompleted()
        with(mBinding.smart) {
            finishRefresh()
            finishLoadMore()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTicketList(workMode, typeId)
    }

    override fun bindListener() {

    }

    override fun onViewStateChange(state: WorkListState) {
        pg.loadDataForPagination(state.list, state.curPage)
    }

    override fun getViewBinding() = ActivityWorkListBinding.inflate(layoutInflater)

}