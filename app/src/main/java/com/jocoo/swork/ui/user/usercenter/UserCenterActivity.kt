package com.jocoo.swork.ui.user.usercenter

import android.os.Bundle
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gdmm.core.BaseCompatActivity
import com.gdmm.core.extensions.simpleActionBar
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.databinding.ActivityUserCenterBinding
import dagger.hilt.android.AndroidEntryPoint

@Route(path = NavHub.USER_CENTER)
@AndroidEntryPoint
class UserCenterActivity :
    BaseCompatActivity<ActivityUserCenterBinding, UserCenterState, UserCenterViewModel>() {

    override val viewModel: UserCenterViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            simpleActionBar(toolbar)
            siChangePassword.setOnClickListener {
                ARouter.getInstance().build(NavHub.CHANGE_PASSWD).navigation()
            }
        }
        viewModel.fetchBaseInfo()
    }

    override fun bindListener() {

    }

    override fun onViewStateChange(state: UserCenterState) {
        mBinding.apply {
            siCompany.setRightText(state.name)
            siAppVersion.setRightText(state.version)
        }
    }

    override fun getViewBinding() = ActivityUserCenterBinding.inflate(layoutInflater)

}