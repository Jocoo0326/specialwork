package com.jocoo.swork.work.audit.gas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gdmm.core.BaseFragment
import com.jocoo.swork.databinding.WorkAuditGasFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuditGasFragment :
    BaseFragment<WorkAuditGasFragmentBinding, AuditGasState, AuditGasViewModel>() {

    override val viewModel: AuditGasViewModel by viewModels()


    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun bindListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = WorkAuditGasFragmentBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: AuditGasState) {

    }
}
