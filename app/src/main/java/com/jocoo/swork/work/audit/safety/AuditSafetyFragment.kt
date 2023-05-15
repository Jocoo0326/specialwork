package com.jocoo.swork.work.audit.safety

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.drake.brv.utils.divider
import com.gdmm.core.BaseFragment
import com.gdmm.core.extensions.observeWithLifecycle
import com.hjq.toast.Toaster
import com.jocoo.swork.R
import com.jocoo.swork.bean.CheckInfo
import com.jocoo.swork.databinding.WorkAuditSafetymeasuresFragmentBinding
import com.jocoo.swork.widget.SignatureDialog
import com.jocoo.swork.work.audit.WorkAuditViewModel
import com.lxj.xpopup.XPopup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuditSafetyFragment :
    BaseFragment<WorkAuditSafetymeasuresFragmentBinding, AuditSafetyState, AuditSafetyViewModel>() {

    private lateinit var mAdapter: AuditSafetyAdapter
    override val viewModel: AuditSafetyViewModel by viewModels()
    private val actViewModel: WorkAuditViewModel by activityViewModels()


    override fun initView(savedInstanceState: Bundle?) {
        binding.apply {
            recyclerView.divider {
                setColorRes(R.color.divider_line)
            }
            mAdapter = AuditSafetyAdapter()
            mAdapter.setOnItemChildClickListener { _, view, pos ->
                if (view.id == R.id.iv_signature) {
                    val item = mAdapter.getItem(pos)
                    if (mAdapter.readyToSign && !item.isConfirmed && item.isSelected) {
                        XPopup.Builder(requireContext())
                            .enableDrag(false)
                            .dismissOnTouchOutside(true)
                            .asCustom(
                                SignatureDialog(
                                    requireContext(),
                                    viewModel,
                                    viewLifecycleOwner
                                )
                            )
                            .show()
                    }
                }
            }
            recyclerView.adapter = mAdapter
            btnAudit.setOnClickListener {
                val list = mAdapter.data
                val selectedList = list.filter { it1 -> !it1.isConfirmed && it1.isSelected }
                if (selectedList.isEmpty()) {
                    Toaster.show("请至少选择一项安全措施")
                    return@setOnClickListener
                }
                viewModel.signMode(true)
            }
            btnDone.setOnClickListener {
                actViewModel.nextPage()
            }
            btnCancelAudit.setOnClickListener {
                viewModel.signMode(false)
            }
            btnConfirm.setOnClickListener {

            }
        }
        actViewModel.state.observeWithLifecycle(this) {
            val checkList = mutableListOf<CheckInfo>()
            checkList.addAll(it.detail?.checkList?.toList() ?: emptyList())
            checkList.addAll(it.detail?.addCheckList?.toList() ?: emptyList())
            mAdapter.setNewInstance(checkList)
            viewModel.hasAuditTodo(checkList.indexOfFirst { it1 -> !it1.isConfirmed } > -1)
        }
    }

    override fun bindListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = WorkAuditSafetymeasuresFragmentBinding.inflate(inflater, container, false)

    override fun onViewStateChange(state: AuditSafetyState) {
        binding.apply {
            btnAudit.visibility =
                if (!state.showOkCancel && state.showAudit) View.VISIBLE else View.GONE
            btnDone.visibility =
                if (!state.showOkCancel && state.showNext) View.VISIBLE else View.GONE
            btnCancelAudit.visibility = if (state.showOkCancel) View.VISIBLE else View.GONE
            btnConfirm.visibility = if (state.showOkCancel) View.VISIBLE else View.GONE
            mAdapter.signMode(state.showOkCancel)
        }
    }
}

