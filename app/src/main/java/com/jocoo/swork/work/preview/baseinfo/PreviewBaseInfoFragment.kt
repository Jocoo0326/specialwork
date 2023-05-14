package com.jocoo.swork.work.preview.baseinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gdmm.core.BaseFragment
import com.jocoo.swork.databinding.WorkPreviewBaseinfoFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewBaseInfoFragment :
    BaseFragment<WorkPreviewBaseinfoFragmentBinding, PreviewBaseInfoState, PreviewBaseInfoViewModel>() {

    override val viewModel: PreviewBaseInfoViewModel by viewModels()


    override fun initView(savedInstanceState: Bundle?) {

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
