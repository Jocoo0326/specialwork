package com.jocoo.swork.work.preview.safety

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.*
import com.gdmm.core.BaseFragment
import com.gdmm.core.extensions.observeWithLifecycle
import com.jocoo.swork.R
import com.jocoo.swork.bean.CheckInfo
import com.jocoo.swork.bean.WorkSign
import com.jocoo.swork.databinding.LayoutWorkerSignItemBinding
import com.jocoo.swork.databinding.LayoutWorkerSignListBinding
import com.jocoo.swork.databinding.WorkPreviewSafetymeasuresFragmentBinding
import com.jocoo.swork.databinding.WorkPreviewSafetymeasuresItemBinding
import com.jocoo.swork.work.audit.WorkAuditViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewSafetyFragment :
    BaseFragment<WorkPreviewSafetymeasuresFragmentBinding, PreviewSafetyState, PreviewSafetyViewModel>() {
    override val viewModel: PreviewSafetyViewModel by viewModels()
    private val actViewModel: WorkAuditViewModel by activityViewModels()

    override fun initView(savedInstanceState: Bundle?) {
        binding.apply {
            recyclerView.divider {
                setColorRes(R.color.divider_line)
            }.setup {
                addType<CheckInfo>(R.layout.work_preview_safetymeasures_item)
                addType<Footer>(R.layout.layout_worker_sign_list)
                onBind {
                    when (val model = getModel<Any>()) {
                        is CheckInfo -> {
                            getBinding<WorkPreviewSafetymeasuresItemBinding>().apply {
                                tvNumber.text = "${modelPosition + 1}"
                                tv1.text = model.content
                                tv2.text = model.isHasStr
                                Glide.with(ivSignature).load(model.sign).into(ivSignature)
                            }
                        }
                        is Footer -> {
                            getBinding<LayoutWorkerSignListBinding>().apply {
                                rvWorkerSign
                                    .grid(spanCount = 2)
                                    .divider {
                                        setDivider(10, true)
                                        orientation = DividerOrientation.GRID
                                    }
                                    .setup {
                                        addType<WorkSign>(R.layout.layout_worker_sign_item)
                                        onBind {
                                            val modelWorkSign = getModel<WorkSign>()
                                            getBinding<LayoutWorkerSignItemBinding>().let {
                                                Glide.with(it.tv1).load(modelWorkSign.sign).into(it.tv1)
                                            }
                                        }
                                    }.models = model.workSignList
                                tv3.visibility =
                                    if (model.workSignList.isNullOrEmpty()) View.VISIBLE else View.GONE
                            }
                        }
                    }
                }
            }
        }
    }

    override fun bindListener() {
        actViewModel.state.observeWithLifecycle(this) {
            val checkList = mutableListOf<CheckInfo>()
            checkList.addAll(it.detail?.checkList?.toList() ?: emptyList())
            checkList.addAll(it.detail?.addCheckList?.toList() ?: emptyList())
            binding.recyclerView.models = checkList
            binding.recyclerView.bindingAdapter.addFooter(Footer(it.detail?.work_sign_list))
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = WorkPreviewSafetymeasuresFragmentBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: PreviewSafetyState) {

    }
}

data class Footer(
    val workSignList: List<WorkSign>? = null
)