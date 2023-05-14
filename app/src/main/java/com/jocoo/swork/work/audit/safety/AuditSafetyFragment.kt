package com.jocoo.swork.work.audit.safety

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gdmm.core.BaseFragment
import com.jocoo.swork.databinding.WorkAuditSafetymeasuresFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuditSafetyFragment :
    BaseFragment<WorkAuditSafetymeasuresFragmentBinding, AuditSafetyState, AuditSafetyViewModel>() {

    override val viewModel: AuditSafetyViewModel by viewModels()


    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun bindListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = WorkAuditSafetymeasuresFragmentBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: AuditSafetyState) {

    }
}

