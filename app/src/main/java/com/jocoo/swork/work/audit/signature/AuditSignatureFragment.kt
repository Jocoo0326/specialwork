package com.jocoo.swork.work.audit.signature


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gdmm.core.BaseFragment
import com.jocoo.swork.databinding.WorkAuditSignatureFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuditSignatureFragment :
    BaseFragment<WorkAuditSignatureFragmentBinding, AuditSignatureState, AuditSignatureViewModel>() {

    override val viewModel: AuditSignatureViewModel by viewModels()


    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun bindListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = WorkAuditSignatureFragmentBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: AuditSignatureState) {

    }
}

