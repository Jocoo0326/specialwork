package com.jocoo.swork.ui.staff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.gdmm.core.BaseFragment
import com.jocoo.swork.R
import com.jocoo.swork.bean.StaffGroupItem
import com.jocoo.swork.databinding.FragmentStaffBinding

class StaffFragment : BaseFragment<FragmentStaffBinding, StaffState, StaffViewModel>() {
    override val viewModel: StaffViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.apply {
            tvCompany.text = "福建三明金氟化工科技有限公司"
            userName.text = "登录帐号: 13700001111"
            recyclerView.linear().setup {
                addType<StaffGroupItem>(R.layout.staff_item_header)
            }
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