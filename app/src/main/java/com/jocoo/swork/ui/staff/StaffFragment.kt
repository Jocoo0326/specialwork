package com.jocoo.swork.ui.staff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gdmm.core.BaseFragment
import com.jocoo.swork.databinding.FragmentStaffBinding

class StaffFragment : BaseFragment<FragmentStaffBinding, StaffState, StaffViewModel>() {
    override val viewModel: StaffViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun bindListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentStaffBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: StaffState) {

    }


}