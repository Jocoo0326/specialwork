package com.jocoo.swork.work.preview.signature

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.drake.brv.utils.divider
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.gdmm.core.BaseFragment
import com.gdmm.core.extensions.observeWithLifecycle
import com.jocoo.swork.R
import com.jocoo.swork.bean.ProcessOpinion
import com.jocoo.swork.databinding.WorkPreviewSignatureFragmentBinding
import com.jocoo.swork.databinding.WorkPreviewSignatureItemBinding
import com.jocoo.swork.work.preview.WorkPreviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewSignatureFragment :
    BaseFragment<WorkPreviewSignatureFragmentBinding, PreviewSignatureState, PreviewSignatureViewModel>() {
    override val viewModel: PreviewSignatureViewModel by viewModels()
    private val actViewModel: WorkPreviewViewModel by activityViewModels()

    override fun initView(savedInstanceState: Bundle?) {
        binding.apply {
            recyclerView.divider {
                setDivider(10, true)
            }.setup {
                addType<ProcessOpinion>(R.layout.work_audit_signature_item)
                onBind {
                    val model = getModel<ProcessOpinion>()
                    when {
                        (model.field == "disclosure_sign" || model.field == "accept_disclosure_sign") -> {
                            getBinding<WorkPreviewSignatureItemBinding>().apply {
                                tvTitle.text = model.name
                                tvComment.visibility = View.GONE
                                tvSignature.text = "暂无签名"
                                Glide.with(context).load(model.sign).into(ivSignature)
                            }
                        }

                        else -> {
                            getBinding<WorkPreviewSignatureItemBinding>().apply {
                                tvTitle.text = model.name
                                tvComment.visibility = View.VISIBLE
                                if (model.content.isNullOrEmpty()) {
                                    tvComment.gravity = Gravity.CENTER
                                    tvComment.text = "暂无意见"
                                } else {
                                    tvComment.gravity = Gravity.START
                                    tvComment.text = model.content
                                }
                                tvSignature.text = "暂无签名"
                                Glide.with(context).load(model.sign).into(ivSignature)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun bindListener() {
        actViewModel.state.observeWithLifecycle(this) {
            it.detail?.let { it ->
                val list = mutableListOf<ProcessOpinion>()
                list.add(
                    ProcessOpinion(
                        name = "安全交底人",
                        field = "disclosure_sign",
                        sign = it.disclosure_sign
                    )
                )
                list.add(
                    ProcessOpinion(
                        name = "接受交底人",
                        field = "accept_disclosure_sign",
                        sign = it.accept_disclosure_sign
                    )
                )
                list.addAll(it.processOpinions?.toList() ?: emptyList())
                binding.recyclerView.models = list
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = WorkPreviewSignatureFragmentBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: PreviewSignatureState) {
    }
}
