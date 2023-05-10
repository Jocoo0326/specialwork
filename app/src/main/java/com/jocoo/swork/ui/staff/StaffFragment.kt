package com.jocoo.swork.ui.staff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.launcher.ARouter
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.gdmm.core.BaseFragment
import com.hjq.toast.Toaster
import com.jocoo.swork.R
import com.jocoo.swork.bean.StaffGroupItem
import com.jocoo.swork.bean.WorkUnitItem
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
                    if (this.modelPosition == 0) {
                        Toaster.show("公司部门")
                    }
                }
                R.id.btnDetail.onClick {
                    ARouter.getInstance().build(NavHub.STAFF_LIST)
                        .navigation()
                }
                addType<WorkUnitItem>(R.layout.staff_item)
                onBind {
                    when (val model = getModel<Any>()) {
                        is StaffGroupItem -> {
                            getBinding<StaffItemHeaderBinding>().tvTitle.text = model.name
                        }

                        is WorkUnitItem -> {
                            getBinding<StaffItemBinding>().tvName.text = model.name
                        }
                    }
                }
            }
        }
        viewModel.fetchUserInfo()
        viewModel.fetchData()
    }

    override fun bindListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentStaffBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: StaffState) {
        binding.apply {
            tvCompany.text = state.name
            userName.text = "登录帐号: ${state.account}"
            recyclerView.models = state.staffGroupList?.toList()
        }
    }


}