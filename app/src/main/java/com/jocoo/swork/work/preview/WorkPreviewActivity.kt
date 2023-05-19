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
import com.google.android.material.tabs.TabLayoutMediator
import com.jocoo.swork.data.COMM_KEY_1
import com.jocoo.swork.data.COMM_KEY_2
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.databinding.ActivityWorkPreviewBinding
import com.jocoo.swork.util.hasGas
import com.jocoo.swork.work.preview.baseinfo.PreviewBaseInfoFragment
import com.jocoo.swork.work.preview.gas.PreviewGasFragment
import com.jocoo.swork.work.preview.safety.PreviewSafetyFragment
import com.jocoo.swork.work.preview.signature.PreviewSignatureFragment
import dagger.hilt.android.AndroidEntryPoint

@Route(path = NavHub.WORK_PREVIEW)
@AndroidEntryPoint
class WorkPreviewActivity :
    BaseCompatActivity<ActivityWorkPreviewBinding, WorkPreviewState, WorkPreviewViewModel>() {

    override val viewModel: WorkPreviewViewModel by viewModels()

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
                ARouter.getInstance()
                    .build(NavHub.WORK_COMPLETE)
                    .withString(COMM_KEY_1, workId)
                    .navigation()
            }
        }
        viewModel.workId = workId
        viewModel.workType = workType
        viewModel.getTicketInfo()
    }

    override fun bindListener() {
    }

    override fun onViewStateChange(state: WorkPreviewState) {
        mBinding.apply {
            toolbar.title = state.detail?.statusStr
            if (state.detail?.isComplete == true) {
                mBinding.llAction.visibility = View.GONE
            } else {
                mBinding.llAction.visibility = View.VISIBLE
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
