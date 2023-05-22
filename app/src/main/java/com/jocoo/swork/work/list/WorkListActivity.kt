package com.jocoo.swork.work.list

import android.os.Bundle
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.drake.brv.utils.divider
import com.gdmm.core.BaseCompatActivity
import com.gdmm.core.extensions.observeWithLifecycle
import com.gdmm.core.extensions.simpleActionBar
import com.jocoo.swork.R
import com.jocoo.swork.bean.AppEvent
import com.jocoo.swork.bean.AppEventType
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
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

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

    @Inject
    lateinit var mEventFlow: MutableSharedFlow<AppEvent>

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
            mAdapter.setOnItemChildClickListener { _, view, pos ->
                if (view.id == R.id.btn_audit) {
                    val data = mAdapter.data[pos]
                    if (workMode == WorkMode.Todo_Id) {
                        ARouter.getInstance().build(NavHub.WORK_AUDIT)
                            .withInt(COMM_KEY_1, typeId)
                            .withString(COMM_KEY_2, data.id)
                            .navigation()
                    } else {
                        ARouter.getInstance().build(NavHub.WORK_PREVIEW)
                            .withInt(COMM_KEY_1, typeId)
                            .withString(COMM_KEY_2, data.id)
                            .navigation()
                    }
                }
            }
            recyclerView.divider {
                setDivider(10, true)
                startVisible = true
            }
            recyclerView.adapter = mAdapter
        }
        pg = PaginationDelegation(mAdapter) { page ->
            viewModel.getTicketList(workMode, typeId, page)
        }
        viewModel.getTicketList(workMode, typeId)
    }

    override fun onLoadCompleted() {
        super.onLoadCompleted()
        with(mBinding.smart) {
            finishRefresh()
            finishLoadMore()
        }
    }

    override fun bindListener() {
        mEventFlow.observeWithLifecycle(this) {
            if (it.type == AppEventType.START_WORK) {
                viewModel.getTicketList(workMode, typeId)
            }
        }
    }

    override fun onViewStateChange(state: WorkListState) {
        pg.loadDataForPagination(state.list, state.curPage)
    }

    override fun getViewBinding() = ActivityWorkListBinding.inflate(layoutInflater)

}