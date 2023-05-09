package com.jocoo.swork.ui.staff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.launcher.ARouter
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.gdmm.core.BaseFragment
import com.jocoo.swork.R
import com.jocoo.swork.bean.StaffChildItem
import com.jocoo.swork.bean.StaffGroupItem
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.databinding.FragmentStaffBinding
import com.jocoo.swork.databinding.StaffItemBinding
import com.jocoo.swork.databinding.StaffItemHeaderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StaffFragment : BaseFragment<FragmentStaffBinding, StaffState, StaffViewModel>() {
    override val viewModel: StaffViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.apply {
            clUserInfo.setOnClickListener {
                ARouter.getInstance().build(NavHub.USER_CENTER).navigation()
            }
            recyclerView.linear().divider {
                setColorRes(R.color.divider_line)
            }.setup {
                addType<StaffGroupItem>(R.layout.staff_item_header)
                R.id.staffItemHeader.onClick {
                    expandOrCollapse()
                }
                R.id.staffItem.onClick {
                    ARouter.getInstance().build(NavHub.STAFF_LIST).navigation()
                }
                addType<StaffChildItem>(R.layout.staff_item)
                onBind {
                    when (val model = getModel<Any>()) {
                        is StaffGroupItem -> {
                            getBinding<StaffItemHeaderBinding>().tvTitle.text =
                                model.name
                        }

                        is StaffChildItem -> {
                            getBinding<StaffItemBinding>().tvName.text =
                                model.name
                        }
                    }
                }
            }.models = mutableListOf(
                StaffGroupItem("公司部门").also { it ->
                    it.itemSublist = listOf(
                        StaffChildItem("福建三明金氟化工科技有限公司1"),
                        StaffChildItem("福建三明金氟化工科技有限公司2"),
                        StaffChildItem("福建三明金氟化工科技有限公司3"),
                        StaffChildItem("福建三明金氟化工科技有限公司4"),
                        StaffChildItem("福建三明金氟化工科技有限公司5"),
                        StaffChildItem("福建三明金氟化工科技有限公司6"),
                        StaffChildItem("福建三明金氟化工科技有限公司7"),
                    )
                },
                StaffGroupItem("承包商").also {
                    it.itemSublist = listOf(
                        StaffChildItem("承包商1"),
                        StaffChildItem("承包商2"),
                        StaffChildItem("承包商3"),
                        StaffChildItem("承包商4"),
                        StaffChildItem("承包商5"),
                        StaffChildItem("承包商6"),
                        StaffChildItem("承包商7"),
                        StaffChildItem("承包商8"),
                        StaffChildItem("承包商9"),
                        StaffChildItem("承包商10"),
                        StaffChildItem("承包商11"),
                        StaffChildItem("承包商12"),
                    )
                }
            )
        }
        viewModel.fetchUserInfo()
    }

    override fun bindListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentStaffBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: StaffState) {
        binding.apply {
            tvCompany.text = state.name
            userName.text = "登录帐号: ${state.account}"
        }
    }


}