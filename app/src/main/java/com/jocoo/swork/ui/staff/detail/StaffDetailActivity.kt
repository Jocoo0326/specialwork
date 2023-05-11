package com.jocoo.swork.ui.staff.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gdmm.core.BaseCompatActivity
import com.gdmm.core.extensions.simpleActionBar
import com.jocoo.swork.data.COMM_KEY_1
import com.jocoo.swork.data.COMM_KEY_2
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.databinding.StaffDetailActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@Route(path = NavHub.STAFF_DETAIL)
@AndroidEntryPoint
class StaffDetailActivity :
    BaseCompatActivity<StaffDetailActivityBinding, StaffDetailState, StaffDetailViewModel>() {

    override val viewModel: StaffDetailViewModel by viewModels()

    @JvmField
    @Autowired(name = COMM_KEY_1)
    var name: String = ""

    @JvmField
    @Autowired(name = COMM_KEY_2)
    var isFace: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            simpleActionBar(toolbar)
            siName.setRightText(name)
            if (isFace == 1) {
                siFace.setRightText("已认证")
                btnEnterFaceId.visibility = View.GONE
            } else {
                siFace.setRightText("未认证")
                btnEnterFaceId.visibility = View.VISIBLE
            }
        }
    }

    override fun bindListener() {

    }

    override fun onViewStateChange(state: StaffDetailState) {

    }

    override fun getViewBinding() = StaffDetailActivityBinding.inflate(layoutInflater)

}