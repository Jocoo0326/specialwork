package com.jocoo.swork.work.audit.gas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.drake.brv.utils.divider
import com.gdmm.core.BaseFragment
import com.gdmm.core.extensions.observeWithLifecycle
import com.jocoo.swork.R
import com.jocoo.swork.databinding.WorkAuditGasFragmentBinding
import com.jocoo.swork.widget.GasAddDialog
import com.jocoo.swork.widget.face.FaceCreateDialog
import com.jocoo.swork.widget.face.FaceViewModel
import com.jocoo.swork.work.audit.WorkAuditViewModel
import com.lxj.xpopup.XPopup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuditGasFragment :
    BaseFragment<WorkAuditGasFragmentBinding, AuditGasState, AuditGasViewModel>() {

    private lateinit var mAdapter: AuditGasAdapter
    override val viewModel: AuditGasViewModel by viewModels()
    private val actViewModel: WorkAuditViewModel by activityViewModels()
    private val faceViewModel: FaceViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        binding.apply {
            recyclerView.divider {
                setDivider(2, true)
            }
            mAdapter = AuditGasAdapter(actViewModel.workType)
            mAdapter.setOnItemChildClickListener { _, view, pos ->
                val item = mAdapter.data[pos]
                when (view.id) {
                    R.id.tv_modify -> {
                        popGasDialog(item.id)
                        mAdapter.notifyItemChanged(pos)
                    }

                    R.id.tv_delete -> {
                        val dialog = MaterialDialog(requireContext())
                        dialog.title(text = "提示").message(text = "确认删除吗?")
                            .negativeButton(text = "取消") {
                                it.dismiss()
                            }.positiveButton(text = "确定") {
                                viewModel.deleteGas(item.id ?: "") {
                                    actViewModel.getTicketInfo()
                                }
                            }.show()
                    }
                }
            }
            recyclerView.adapter = mAdapter
            btnDone.setOnClickListener {
                faceViewModel.initCheck(actViewModel.workId, FaceViewModel.GAS_FIELD)
                XPopup.Builder(requireContext())
                    .enableDrag(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(
                        FaceCreateDialog(requireActivity(), faceViewModel)
                    ).show()
            }
            btnManualAdd.setOnClickListener {
                popGasDialog()
            }
        }
        actViewModel.state.observeWithLifecycle(this) {
            mAdapter.setNewInstance(it.detail?.sensorDataList?.toMutableList())
        }
    }

    private fun showGasDialog(id: String? = null) {
        XPopup.Builder(requireActivity()).enableDrag(false).isViewMode(true)
            .dismissOnTouchOutside(true).asCustom(
                GasAddDialog(
                    requireActivity(), id, actViewModel, viewLifecycleOwner
                )
            ).show()
    }

    private fun popGasDialog(id: String? = null) {
        if (actViewModel.state.value.gasConfig == null) {
            actViewModel.fetchGasConfig {
                showGasDialog(id)
            }
        } else {
            showGasDialog(id)
        }
    }

    override fun bindListener() {
        actViewModel.modifyGasFlow.observeWithLifecycle(this) {
            actViewModel.state.value.detail?.sensorDataList?.indexOfFirst { it1 -> it1.id == it }
                ?.let {
                    mAdapter.notifyItemChanged(it)
                }
        }
        faceViewModel.faceFlow.observeWithLifecycle(this) {
            if (it == FaceViewModel.success_msg) {
                actViewModel.nextPage()
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = WorkAuditGasFragmentBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: AuditGasState) {

    }
}
