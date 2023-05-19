package com.jocoo.swork.work.preview.baseinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.gdmm.core.BaseFragment
import com.gdmm.core.extensions.observeWithLifecycle
import com.jocoo.swork.databinding.WorkPreviewBaseinfoFragmentBinding
import com.jocoo.swork.util.fillBaseInfo
import com.jocoo.swork.util.getTicketTitle
import com.jocoo.swork.work.audit.WorkAuditViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewBaseInfoFragment :
    BaseFragment<WorkPreviewBaseinfoFragmentBinding, PreviewBaseInfoState, PreviewBaseInfoViewModel>() {

    override val viewModel: PreviewBaseInfoViewModel by viewModels()
    private val actViewModel: WorkAuditViewModel by activityViewModels()

    override fun initView(savedInstanceState: Bundle?) {
        actViewModel.state.observeWithLifecycle(this) {
            binding.apply {
                tvTitleBaseInfo.text = getTicketTitle(actViewModel.workType)
                llContainer.removeAllViews()
                fillBaseInfo(actViewModel.workType, llContainer, it.detail)
            }
        }
    }

    override fun bindListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = WorkPreviewBaseinfoFragmentBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: PreviewBaseInfoState) {

    }
}
