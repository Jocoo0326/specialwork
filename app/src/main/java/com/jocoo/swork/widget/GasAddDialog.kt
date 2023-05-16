package com.jocoo.swork.widget

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.jocoo.swork.R
import com.jocoo.swork.work.audit.gas.AuditGasViewModel
import com.lxj.xpopup.core.BottomPopupView

class GasAddDialog(
    context: Context,
    private val viewModel: AuditGasViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
) : BottomPopupView(context) {

    override fun onCreate() {
        super.onCreate()
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_gas_add
    }
}