package com.jocoo.swork.ui.user.changepassword

import android.os.Bundle
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.gdmm.core.BaseCompatActivity
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.databinding.ActivityChangePasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@Route(path = NavHub.CHANGE_PASSWD)
@AndroidEntryPoint
class ChangePasswordActivity :
    BaseCompatActivity<ActivityChangePasswordBinding, ChangePasswordState, ChangePasswordViewModel>() {

    override val viewModel: ChangePasswordViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun bindListener() {

    }

    override fun onViewStateChange(state: ChangePasswordState) {

    }

    override fun getViewBinding() = ActivityChangePasswordBinding.inflate(layoutInflater)

}