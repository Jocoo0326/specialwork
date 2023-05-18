package com.jocoo.swork.work.preview.gas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gdmm.core.BaseFragment
import com.jocoo.swork.databinding.WorkPreviewGasFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewGasFragment :
    BaseFragment<WorkPreviewGasFragmentBinding, PreviewGasState, PreviewGasViewModel>() {
    override val viewModel: PreviewGasViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun bindListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = WorkPreviewGasFragmentBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: PreviewGasState) {

    }
}
