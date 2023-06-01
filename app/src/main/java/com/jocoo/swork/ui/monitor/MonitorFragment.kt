package com.jocoo.swork.ui.monitor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gdmm.core.BaseFragment
import com.jocoo.swork.databinding.FragmentMonitorBinding

class MonitorFragment : BaseFragment<FragmentMonitorBinding, MonitorState, MonitorViewModel>() {
    override val viewModel: MonitorViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun bindListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentMonitorBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: MonitorState) {

    }


}