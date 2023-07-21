package com.jocoo.swork.work.audit.signature


import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.gdmm.core.BaseFragment
import com.gdmm.core.extensions.observeWithLifecycle
import com.hjq.toast.Toaster
import com.jocoo.swork.R
import com.jocoo.swork.bean.*
import com.jocoo.swork.databinding.WorkAuditSignatureFragmentBinding
import com.jocoo.swork.databinding.WorkAuditSignatureItemBinding
import com.jocoo.swork.util.showProcessLimits
import com.jocoo.swork.widget.CommonInputDialog
import com.jocoo.swork.widget.SignatureDialog
import com.jocoo.swork.widget.UploadImageViewModel
import com.jocoo.swork.widget.face.FaceCreateDialog
import com.jocoo.swork.widget.face.FaceViewModel
import com.jocoo.swork.work.audit.WorkAuditViewModel
import com.lxj.xpopup.XPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@AndroidEntryPoint
class AuditSignatureFragment :
    BaseFragment<WorkAuditSignatureFragmentBinding, AuditSignatureState, AuditSignatureViewModel>() {

    private var curModel: SignInfo? = null
    override val viewModel: AuditSignatureViewModel by viewModels()
    private val actViewModel: WorkAuditViewModel by activityViewModels()
    private val uploadImageViewModel: UploadImageViewModel by viewModels()
    private val _userInputFlow = MutableSharedFlow<String>(
        replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val userInputFlow = _userInputFlow.asSharedFlow()

    @Inject
    lateinit var mEventFlow: MutableSharedFlow<AppEvent>

    private val faceViewModel: FaceViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        binding.apply {
            recyclerView.divider {
                setDivider(10, true)
            }.setup {
                addType<SignOption>(R.layout.work_audit_signature_item)
                addType<ProcessOpinion>(R.layout.work_audit_signature_item)
                R.id.fl_signature.onClick {
                    val model = getModel<Any>() as SignInfo
                    curModel = model
                    if (model is ProcessOpinion && model.isFace && model.sign.isNullOrEmpty()) {
                        faceViewModel.initCheck(actViewModel.workId, model.field ?: "")
                        requireContext().showProcessLimits(faceViewModel) {
                            XPopup.Builder(requireContext())
                                .enableDrag(false)
                                .dismissOnTouchOutside(false)
                                .asCustom(
                                    FaceCreateDialog(requireActivity(), faceViewModel)
                                ).show()
                        }
                    } else {
                        showSignatureDialog()
                    }
                }
                R.id.tv_comment.onClick {
                    val model = getModel<Any>() as SignInfo
                    curModel = model
                    XPopup.Builder(requireContext()).enableDrag(false).dismissOnTouchOutside(false)
                        .asCustom(
                            CommonInputDialog(
                                requireContext(), "意见", _userInputFlow
                            )
                        ).show()
                }
                onBind {
                    when (val model = getModel<Any>()) {
                        is SignOption -> {
                            getBinding<WorkAuditSignatureItemBinding>().apply {
                                tvTitle.text = model.name
                                tvComment.visibility = View.GONE
                                tvSignature.text = "点击签名"
                                Glide.with(context).load(model.sign).into(ivSignature)
                            }
                        }

                        is ProcessOpinion -> {
                            getBinding<WorkAuditSignatureItemBinding>().apply {
                                tvTitle.text = model.name
                                tvComment.visibility = View.VISIBLE
                                if (model.comment.isNullOrEmpty()) {
                                    tvComment.gravity = Gravity.CENTER
                                    tvComment.text = ""
                                } else {
                                    tvComment.gravity = Gravity.START
                                    tvComment.text = model.comment
                                }
                                tvSignature.text = "点击签名"
                                Glide.with(context).load(model.sign).into(ivSignature)
                            }
                        }
                    }
                }
            }
        }
        viewModel.getTicketOpinions(actViewModel.workId)
    }

    private fun showSignatureDialog() {
        XPopup.Builder(requireContext()).enableDrag(false)
            .dismissOnTouchOutside(false)
            .asCustom(
                SignatureDialog(
                    requireContext(), uploadImageViewModel, viewLifecycleOwner
                )
            ).show()

    }

    override fun bindListener() {
        uploadImageViewModel.uploadImageFlow.observeWithLifecycle(this) {
            if (curModel != null) {
                curModel?.sign = it
                val index =
                    binding.recyclerView.models?.indexOfFirst { it1 -> it1 == curModel } ?: -1
                if (index > -1) {
                    binding.recyclerView.bindingAdapter.notifyItemChanged(index)
                }
                curModel = null
            }
        }
        userInputFlow.observeWithLifecycle(this) {
            if (curModel != null) {
                curModel?.comment = it
                val index =
                    binding.recyclerView.models?.indexOfFirst { it1 -> it1 == curModel } ?: -1
                if (index > -1) {
                    binding.recyclerView.bindingAdapter.notifyItemChanged(index)
                }
                curModel = null
            }
        }
        viewModel.setOpinionFlow.observeWithLifecycle(this) {
            requireActivity().finish()
            mEventFlow.tryEmit(AppEvent(AppEventType.START_WORK))
        }
        binding.btnDone.setOnClickListener {
            val list = binding.recyclerView.models
            list?.filterIsInstance<ProcessOpinion>()?.firstOrNull { item ->
                item.isFace && item.sign.isNullOrEmpty()
            }?.let {
                Toaster.show("请先进行签名: ${it.name}")
                return@setOnClickListener
            }
            viewModel.setOpinions(actViewModel.workId, list)
        }
        faceViewModel.faceFlow.observeWithLifecycle(this) {
            if (it.msg == FaceViewModel.success_msg) {
                if (curModel != null) {
                    curModel?.faceResult = it
                }
                showSignatureDialog()
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = WorkAuditSignatureFragmentBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: AuditSignatureState) {
        val list = mutableListOf<SignInfo>()
        list.addAll(state.optionsList?.signList?.toList() ?: emptyList())
        val newOpinionList = actViewModel.state.value.detail?.processOpinions?.filter {
            if (it.id == 1) it.is_need_face = "1"
            it.field != FaceViewModel.FINISH_FIELD
        } ?: emptyList()
        list.addAll(newOpinionList)
        binding.recyclerView.models = list
    }
}

