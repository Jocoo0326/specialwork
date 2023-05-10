package com.jocoo.swork.ui.user.changepassword

import android.os.Bundle
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.gdmm.core.BaseCompatActivity
import com.gdmm.core.extensions.addTextChangedListener
import com.gdmm.core.extensions.observeWithLifecycle
import com.gdmm.core.extensions.simpleActionBar
import com.hjq.toast.Toaster
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.databinding.ActivityChangePasswordBinding
import com.jocoo.swork.util.reLogin
import dagger.hilt.android.AndroidEntryPoint

@Route(path = NavHub.CHANGE_PASSWD)
@AndroidEntryPoint
class ChangePasswordActivity :
    BaseCompatActivity<ActivityChangePasswordBinding, ChangePasswordState, ChangePasswordViewModel>() {

    override val viewModel: ChangePasswordViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            simpleActionBar(toolbar)
            resetLoginBtnStatus()
            siOldPwd.getRightEditText().addTextChangedListener {
                resetLoginBtnStatus()
            }
            siNewPwd.getRightEditText().addTextChangedListener {
                resetLoginBtnStatus()
            }
            btnSubmit.setOnClickListener {
                val oldPwdStr = mBinding.siOldPwd.getRightEditText().text.toString()
                val newPwdStr = mBinding.siNewPwd.getRightEditText().text.toString()
                viewModel.changePassword(oldPwdStr, newPwdStr)
            }
        }
    }

    private fun resetLoginBtnStatus() {
        val oldPwdStr = mBinding.siOldPwd.getRightEditText().text.toString()
        val newPwdStr = mBinding.siNewPwd.getRightEditText().text.toString()
        mBinding.btnSubmit.isEnabled = oldPwdStr.isNotBlank() && newPwdStr.isNotBlank()
    }

    override fun bindListener() {
        viewModel.changePwdFlow.observeWithLifecycle(this) {
            if (it) {
                Toaster.show("登录失效,请重新登录")
                reLogin()
                finish()
            }
        }
    }

    override fun onViewStateChange(state: ChangePasswordState) {
    }

    override fun getViewBinding() = ActivityChangePasswordBinding.inflate(layoutInflater)

}