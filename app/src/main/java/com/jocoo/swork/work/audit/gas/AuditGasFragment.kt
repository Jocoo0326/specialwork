package com.jocoo.swork.work.audit.gas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.drake.brv.utils.divider
import com.gdmm.core.BaseFragment
import com.gdmm.core.extensions.observeWithLifecycle
import com.jocoo.swork.databinding.WorkAuditGasFragmentBinding
import com.jocoo.swork.work.audit.WorkAuditViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuditGasFragment :
    BaseFragment<WorkAuditGasFragmentBinding, AuditGasState, AuditGasViewModel>() {

    private lateinit var mAdapter: AuditGasAdapter
    override val viewModel: AuditGasViewModel by viewModels()
    private val actViewModel: WorkAuditViewModel by activityViewModels()

    override fun initView(savedInstanceState: Bundle?) {
        binding.apply {
            recyclerView.divider {
                setDivider(15, true)
            }
            mAdapter = AuditGasAdapter(actViewModel.workType)
            recyclerView.adapter = mAdapter
        }
        actViewModel.state.observeWithLifecycle(this) {
            mAdapter.setNewInstance(it.detail?.sensorDataList?.toMutableList())
        }
    }

    override fun bindListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = WorkAuditGasFragmentBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: AuditGasState) {

    }
}
