package com.jocoo.swork.ui.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gdmm.core.BaseFragment
import com.jocoo.swork.databinding.FragmentDeviceBinding

class DeviceFragment : BaseFragment<FragmentDeviceBinding, DeviceState, DeviceViewModel>() {
    override val viewModel: DeviceViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun bindListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentDeviceBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: DeviceState) {

    }


}