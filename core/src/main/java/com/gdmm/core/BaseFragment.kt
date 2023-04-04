package com.gdmm.core

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.gdmm.core.error.onResponseOperator

import com.gdmm.core.extensions.observeWithLifecycle


abstract class BaseFragment<VB : ViewBinding, STATE : State, VM : BaseViewModel<STATE>> :
    Fragment() {

    /*ViewBinding*/
    private lateinit var _binding: VB
    val binding: VB get() = _binding

    //ViewModel
    protected abstract val viewModel: VM

    private var progress: Dialog? = null

    protected open val minActiveState: Lifecycle.State = Lifecycle.State.STARTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState)
        bindListener()
        observeState()
    }

    private fun observeState() {
        viewModel.state.observeWithLifecycle(viewLifecycleOwner, minActiveState = minActiveState) {
            onViewStateChange(it)
        }

        observeEventFlow()
    }

    protected open fun observeEventFlow() {
        viewModel.eventFlows.observeWithLifecycle(viewLifecycleOwner) { event ->
            context?.onResponseOperator(event,
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

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun bindListener()

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    open fun showLoading() {
        if (progress?.isShowing == true) {
            progress?.dismiss()
            progress = null
        }

        progress = MaterialProgressDialog(requireContext()).show()
    }

    protected open fun onError(code: Int, msg: String?): Boolean {
        return false
    }

    protected open fun onSuccess(tag: String) {
        //请求执行成功
    }

    open fun onLoadCompleted() {
        progress?.dismiss()
    }

    abstract fun onViewStateChange(state: STATE)

    override fun onDestroyView() {
        super.onDestroyView()
        onLoadCompleted()
    }
}


class MaterialProgressDialog(val context: Context) {

    fun show(cancellable: Boolean = true): Dialog {
        val dialog = Dialog(context, R.style.Core_Progressbar_Circle)
        with(dialog) {
            setCancelable(cancellable)
            setContentView(R.layout.core_dialog_progress_material)
            show()
        }
        return dialog
    }
}
