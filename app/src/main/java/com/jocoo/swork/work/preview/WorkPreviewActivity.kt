package com.jocoo.swork.work.preview

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gdmm.core.BaseCompatActivity
import com.gdmm.core.extensions.observeWithLifecycle
import com.google.android.material.tabs.TabLayoutMediator
import com.jocoo.swork.data.COMM_KEY_1
import com.jocoo.swork.data.COMM_KEY_2
import com.jocoo.swork.data.COMM_KEY_3
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.databinding.ActivityWorkPreviewBinding
import com.jocoo.swork.util.hasGas
import com.jocoo.swork.work.audit.WorkAuditState
import com.jocoo.swork.work.audit.WorkAuditViewModel
import com.jocoo.swork.work.preview.baseinfo.PreviewBaseInfoFragment
import com.jocoo.swork.work.preview.gas.PreviewGasFragment
import com.jocoo.swork.work.preview.safety.PreviewSafetyFragment
import com.jocoo.swork.work.preview.signature.PreviewSignatureFragment
import dagger.hilt.android.AndroidEntryPoint

@Route(path = NavHub.WORK_PREVIEW)
@AndroidEntryPoint
class WorkPreviewActivity :
    BaseCompatActivity<ActivityWorkPreviewBinding, WorkAuditState, WorkAuditViewModel>() {

    override val viewModel: WorkAuditViewModel by viewModels()

    @JvmField
    @Autowired(name = COMM_KEY_1)
    var workType: Int = 0

    @JvmField
    @Autowired(name = COMM_KEY_2)
    var workId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        super.onCreate(savedInstanceState)
    }

    private val fragList = mutableListOf<Pair<String, Fragment>>()
    override fun initView(savedInstanceState: Bundle?) {
        fragList.add(Pair("基本信息", PreviewBaseInfoFragment()))
        fragList.add(Pair("安全措施", PreviewSafetyFragment()))
        if (hasGas(workType)) {
            fragList.add(Pair("气体分析表", PreviewGasFragment()))
        }
        fragList.add(Pair("审批意见", PreviewSignatureFragment()))
        val frags = fragList.map { it.second }
        mBinding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            viewPager.isUserInputEnabled = false
            viewPager.adapter = WorkPreviewViewPagerAdapter(this@WorkPreviewActivity, frags)
            TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
                tab.text = fragList[pos].first
            }.attach()
            btnComplete.setOnClickListener {
                val fireRank = viewModel.state.value.detail?.special_content?.fire_rank ?: ""
                ARouter.getInstance()
                    .build(NavHub.WORK_COMPLETE)
                    .withString(COMM_KEY_1, workId)
                    .withInt(COMM_KEY_2, workType)
                    .withString(COMM_KEY_3, fireRank)
                    .navigation()
            }
            btnInterrupt.setOnClickListener {
                viewModel.setStop(true)
            }
            btnRestore.setOnClickListener {
                viewModel.setStop(false)
            }
        }
        viewModel.workId = workId
        viewModel.workType = workType
        viewModel.getTicketInfo()
    }

    override fun bindListener() {
        viewModel.setStopFlow.observeWithLifecycle(this) {
            if (it) {
                mBinding.btnRestore.visibility = View.VISIBLE
                mBinding.btnInterrupt.visibility = View.GONE
                mBinding.btnComplete.visibility = View.GONE
                viewModel.state.value.detail?.is_stop = "1"
            } else {
                mBinding.btnRestore.visibility = View.GONE
                mBinding.btnInterrupt.visibility = View.VISIBLE
                mBinding.btnComplete.visibility = View.VISIBLE
                viewModel.state.value.detail?.is_stop = "0"
            }
        }
    }

    override fun onViewStateChange(state: WorkAuditState) {
        mBinding.apply {
            toolbar.title = state.detail?.statusStr
            if (state.detail == null || state.detail.isComplete) {
                mBinding.llAction.visibility = View.GONE
            } else {
                mBinding.llAction.visibility = View.VISIBLE
                if (state.detail.is_stop == "0") {
                    mBinding.btnInterrupt.visibility = View.VISIBLE
                    mBinding.btnRestore.visibility = View.GONE
                    mBinding.btnComplete.visibility = View.VISIBLE
                } else {
                    mBinding.btnInterrupt.visibility = View.GONE
                    mBinding.btnRestore.visibility = View.VISIBLE
                    mBinding.btnComplete.visibility = View.GONE
                }
            }
        }
    }

    override fun getViewBinding() = ActivityWorkPreviewBinding.inflate(layoutInflater)

}

class WorkPreviewViewPagerAdapter(context: FragmentActivity, private val frags: List<Fragment>) :
    FragmentStateAdapter(context) {
    override fun getItemCount(): Int {
        return frags.size
    }

    override fun createFragment(position: Int): Fragment {
        return frags[position]
    }

}
