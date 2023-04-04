package com.gdmm.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.gdmm.core.error.onResponseOperator
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gdmm.core.extensions.observeWithLifecycle

abstract class BaseBottomSheetDialogFragment<VB : ViewBinding, STATE : State, VM : BaseViewModel<STATE>> :
    BottomSheetDialogFragment() {

    /*ViewBinding*/
    private lateinit var _binding: VB
    val binding: VB get() = _binding

    //ViewModel
    protected abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        bindListener()
        observeState()
    }

    private fun observeState() {
        viewModel.state.observeWithLifecycle(viewLifecycleOwner) {
            onViewStateChange(it)
        }

        viewModel.eventFlows.observeWithLifecycle(viewLifecycleOwner) { event ->
            context?.onResponseOperator(event)
        }
    }

    abstract fun initView()

    abstract fun bindListener()

    abstract fun onViewStateChange(state: STATE)

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
}