package com.jocoo.swork.ui.login

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import com.alibaba.android.arouter.launcher.ARouter
import com.gdmm.core.BaseCompatActivity
import com.gdmm.core.extensions.addTextChangedListener
import com.gdmm.core.extensions.observeWithLifecycle
import com.gdmm.core.extensions.setStatusBar
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseCompatActivity<ActivityLoginBinding, LoginState, LoginViewModel>() {
    override val viewModel: LoginViewModel by viewModels()


    override fun initView(savedInstanceState: Bundle?) {
        setStatusBar(statusBarColor = Color.TRANSPARENT, darkTheme = false)
        viewModel.fetchLastAccountInfo()
    }

    override fun bindListener() {
        mBinding.et1.addTextChangedListener {
            resetLoginBtnStatus()
        }
        mBinding.et2.addTextChangedListener {
            resetLoginBtnStatus()
        }
        mBinding.btnLogin.setOnClickListener {
            val account = mBinding.et1.text.toString()
            val pwd = mBinding.et2.text.toString()
            val savePwd = mBinding.cbRememberPwd.isChecked
            viewModel.login(account, pwd, savePwd)
        }
        viewModel.loginFlow.observeWithLifecycle(this) {
            if (it) {
                ARouter.getInstance().build(NavHub.MAIN_INDEX).navigation()
                finish()
            }
        }
    }

    private fun resetLoginBtnStatus() {
        val account = mBinding.et1.text.toString()
        val pwd = mBinding.et2.text.toString()
        mBinding.btnLogin.isEnabled = account.isNotBlank() && pwd.isNotBlank()
    }

    override val minActiveState = Lifecycle.State.CREATED

    override fun onViewStateChange(state: LoginState) {
        mBinding.apply {
            et1.setText(state.account)
            et2.setText(state.pwd)
            cbRememberPwd.isChecked = state.savePwd
        }
    }

    override fun delayLoading(): Boolean {
        return false
    }

    override fun getViewBinding() = ActivityLoginBinding.inflate(layoutInflater)

}