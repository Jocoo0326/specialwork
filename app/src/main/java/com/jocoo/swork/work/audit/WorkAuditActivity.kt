package com.jocoo.swork.work.audit

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.afollestad.materialdialogs.MaterialDialog
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gdmm.core.BaseCompatActivity
import com.gdmm.core.extensions.observeWithLifecycle
import com.jocoo.swork.data.COMM_KEY_1
import com.jocoo.swork.data.COMM_KEY_2
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.databinding.ActivityWorkAuditBinding
import com.jocoo.swork.util.hasGas
import com.jocoo.swork.work.audit.baseinfo.AuditBaseInfoFragment
import com.jocoo.swork.work.audit.gas.AuditGasFragment
import com.jocoo.swork.work.audit.safety.AuditSafetyFragment
import com.jocoo.swork.work.audit.signature.AuditSignatureFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Integer.min

@Route(path = NavHub.WORK_AUDIT)
@AndroidEntryPoint
class WorkAuditActivity :
    BaseCompatActivity<ActivityWorkAuditBinding, WorkAuditState, WorkAuditViewModel>() {

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
        fragList.add(Pair("基本信息", AuditBaseInfoFragment()))
        fragList.add(Pair("安全措施", AuditSafetyFragment()))
        if (hasGas(workType)) {
            fragList.add(Pair("气体分析表", AuditGasFragment()))
        }
        fragList.add(Pair("审批意见", AuditSignatureFragment()))
        val frags = fragList.map { it.second }
        mBinding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            viewPager.adapter = WorkAuditViewPagerAdapter(this@WorkAuditActivity, frags)
        }
        viewModel.workId = workId
        viewModel.workType = workType
        viewModel.getTicketInfo()
    }

    override fun onBackPressed() {
        if (!prePage()) {
            val dialog = MaterialDialog(this)
            dialog.title(text = "提示")
            dialog.message(text = "确认退出吗?")
            dialog.positiveButton(text = "确定") {
                super.onBackPressed()
            }
            dialog.negativeButton(text = "取消") {
                it.dismiss()
            }
            dialog.show()
        }
    }

    private fun prePage(): Boolean {
        if (mBinding.viewPager.currentItem > 0) {
            mBinding.viewPager.setCurrentItem(mBinding.viewPager.currentItem - 1, false)
            return true
        }
        return false
    }

    private fun nextPage() {
        val i = mBinding.viewPager.currentItem
        val count = mBinding.viewPager.adapter?.itemCount ?: 0
        mBinding.viewPager.setCurrentItem(min(i + 1, count), false)
    }

    override fun bindListener() {
        viewModel.nextPageFlow.observeWithLifecycle(this) {
            nextPage()
        }
    }

    override fun onViewStateChange(state: WorkAuditState) {

    }

    override fun getViewBinding() = ActivityWorkAuditBinding.inflate(layoutInflater)

}

class WorkAuditViewPagerAdapter(context: FragmentActivity, private val frags: List<Fragment>) :
    FragmentStateAdapter(context) {
    override fun getItemCount(): Int {
        return frags.size
    }

    override fun createFragment(position: Int): Fragment {
        return frags[position]
    }

}
