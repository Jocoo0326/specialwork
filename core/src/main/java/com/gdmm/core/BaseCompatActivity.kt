package com.gdmm.core

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.gdmm.core.error.onResponseOperator
import com.gdmm.core.extensions.observeWithLifecycle

abstract class BaseCompatActivity<VB : ViewBinding, STATE : State, VM : BaseViewModel<STATE>> :
    BaseActivity<VB>() {

    protected abstract val viewModel: VM

    protected open val minActiveState: Lifecycle.State = Lifecycle.State.STARTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(savedInstanceState)
        bindListener()
        observeState()
    }

    private fun observeState() {//
        viewModel.state.observeWithLifecycle(lifecycle, lifecycleScope, minActiveState) {
            onViewStateChange(it)
        }
        observeEventFlow()
    }

    protected open fun observeEventFlow() {
        viewModel.eventFlows.observeWithLifecycle(lifecycle, lifecycleScope) { event ->
            onResponseOperator(event,
                onLoading = {
                    showLoading()
                }, onFailed = lit@{ code, message ->
                    return@lit onError(code, message)
                }, onSuccess = {
                    onSuccess(it)
                }, onCompleted = {
                    onLoadCompleted()
                })
        }
    }

    /**
     * 返回页面的结果处理
     */
    open var resultLauncher: ActivityResultLauncher<Intent>? = null

    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun bindListener()
    abstract fun onViewStateChange(state: STATE)


    protected open fun onError(code: Int, msg: String?): Boolean {
        return false
    }

    protected open fun onSuccess(tag: String) {
        //请求执行成功
    }


}