package com.jocoo.swork.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gdmm.core.BaseFragment
import com.jocoo.swork.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeState, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun bindListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: HomeState) {

    }


}