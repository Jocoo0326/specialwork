package com.jocoo.swork.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.launcher.ARouter
import com.drake.brv.utils.divider
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.gdmm.core.BaseFragment
import com.jocoo.swork.R
import com.jocoo.swork.bean.HomeItem
import com.jocoo.swork.data.COMM_KEY_1
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.data.enum.WorkMode
import com.jocoo.swork.databinding.FragmentHomeBinding
import com.jocoo.swork.databinding.HomeItemBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeState, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.apply {
            recyclerView.divider {
                setDivider(20, true)
            }.setup {
                addType<HomeItem>(R.layout.home_item)
                onBind {
                    val model = getModel<HomeItem>()
                    getBinding<HomeItemBinding>().apply {
                        bg.setImageResource(model.bgResId ?: R.drawable.home_bg_item2)
                        tvTitle.text = model.name
                        tvTitle.setTextColor(model.contentColor ?: Color.BLACK)
                        tvDot.visibility = if ((model.total ?: 0) > 0) View.VISIBLE else View.GONE
                        tvDot.text = "${model.total}"
                        tvDot.setBackgroundResource(model.dotBgRes)
                    }
                }
                R.id.cv_content.onClick {
                    val model = getModel<HomeItem>()
                    ARouter.getInstance().build(NavHub.WORK_TYPE)
                        .withInt(COMM_KEY_1, model.id ?: WorkMode.Done_Id)
                        .navigation()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchStatistic()
    }

    override fun bindListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: HomeState) {
        binding.recyclerView.models = state.list
    }


}