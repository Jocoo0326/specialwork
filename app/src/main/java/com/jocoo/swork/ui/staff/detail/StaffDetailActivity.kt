package com.jocoo.swork.ui.staff.detail

import android.os.Bundle
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.gdmm.core.BaseCompatActivity
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.databinding.StaffDetailActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@Route(path = NavHub.STAFF_DETAIL)
@AndroidEntryPoint
class StaffDetailActivity :
    BaseCompatActivity<StaffDetailActivityBinding, StaffDetailState, StaffDetailViewModel>() {

    override val viewModel: StaffDetailViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun bindListener() {

    }

    override fun onViewStateChange(state: StaffDetailState) {

    }

    override fun getViewBinding() = StaffDetailActivityBinding.inflate(layoutInflater)

}