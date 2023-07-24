package com.jocoo.swork.work.type

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.divider
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.gdmm.core.BaseCompatActivity
import com.gdmm.core.extensions.observeWithLifecycle
import com.gdmm.core.extensions.simpleActionBar
import com.jocoo.swork.R
import com.jocoo.swork.bean.AppEvent
import com.jocoo.swork.bean.AppEventType
import com.jocoo.swork.bean.WorkTypeInfo
import com.jocoo.swork.data.COMM_KEY_1
import com.jocoo.swork.data.COMM_KEY_2
import com.jocoo.swork.data.COMM_KEY_3
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.data.enum.WorkMode
import com.jocoo.swork.data.enum.WorkType
import com.jocoo.swork.databinding.ActivityWorkTypeBinding
import com.jocoo.swork.databinding.WorkTypeItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@Route(path = NavHub.WORK_TYPE)
@AndroidEntryPoint
class WorkTypeActivity :
    BaseCompatActivity<ActivityWorkTypeBinding, WorkTypeState, WorkTypeViewModel>() {

    override val viewModel: WorkTypeViewModel by viewModels()

    @JvmField
    @Autowired(name = COMM_KEY_1)
    var workMode: Int = WorkMode.Todo_Id

    @Inject
    lateinit var mEventFlow: MutableSharedFlow<AppEvent>

    override fun onCreate(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            simpleActionBar(toolbar)
            changeTitle()
            recyclerView.divider {
                setDivider(32, true)
                orientation = DividerOrientation.HORIZONTAL
            }.setup {
                addType<WorkTypeInfo>(R.layout.work_type_item)
                onBind {
                    val model = getModel<WorkTypeInfo>()
                    getBinding<WorkTypeItemBinding>().apply {
                        tvName.text = model.name
                        ivIcon.setImageResource(
                            model.bgResId ?: R.drawable.work_type_ic_item_blockload
                        )
                        tvDot.visibility = if ((model.total ?: 0) > 0) View.VISIBLE else View.GONE
                        tvDot.text = "${model.total}"
                        tvDot.setBackgroundResource(WorkMode.parseWorkMode(workMode).dotBgRes)
                    }
                }
                R.id.cl_content.onClick {
                    val model = getModel<WorkTypeInfo>()
                    ARouter.getInstance().build(NavHub.WORK_LIST)
                        .withInt(COMM_KEY_1, workMode)
                        .withInt(COMM_KEY_2, model.type_id ?: WorkType.Fire_Id)
                        .withString(COMM_KEY_3, "${model.name}")
                        .navigation()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchStatistic(workMode)
    }

    private fun changeTitle() {
        mBinding.toolbar.title = WorkMode.parseWorkMode(workMode).name + "类型"
    }

    override fun bindListener() {
        mEventFlow.observeWithLifecycle(this, minActiveState = Lifecycle.State.CREATED) {
            if (it.type == AppEventType.START_WORK) {
                workMode = WorkMode.Doing_Id
                changeTitle()
                viewModel.fetchStatistic(workMode)
            } else if (it.type == AppEventType.CHANGE_TO_DONE_WORK_TYPE) {
                workMode = WorkMode.Done_Id
            }
            changeTitle()
            viewModel.fetchStatistic(workMode)
        }
    }

    override fun onViewStateChange(state: WorkTypeState) {
        mBinding.recyclerView.models = state.list
    }

    override fun getViewBinding() = ActivityWorkTypeBinding.inflate(layoutInflater)

}