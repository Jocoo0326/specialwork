package com.jocoo.swork.widget

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.gdmm.core.extensions.observeWithLifecycle
import com.jocoo.swork.R
import com.jocoo.swork.util.toByteArray
import com.jocoo.swork.work.audit.safety.AuditSafetyViewModel
import com.kyanogen.signatureview.SignatureView
import com.lxj.xpopup.core.BottomPopupView

class SignatureDialog(
    context: Context,
    private val viewModel: AuditSafetyViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
) : BottomPopupView(context) {

    override fun onCreate() {
        super.onCreate()
        val signatureView = findViewById<SignatureView>(R.id.sv_signature)
        val cancelBtn = findViewById<View>(R.id.btn_cancel)
        val okBtn = findViewById<View>(R.id.btn_confirm)
        val resetBtn = findViewById<View>(R.id.iv_delete)
        resetBtn.setOnClickListener {
            signatureView.clearCanvas()
        }
        cancelBtn.setOnClickListener {
            dismiss()
        }
        okBtn.setOnClickListener {
            viewModel.uploadImage(signatureView.signatureBitmap.toByteArray())
        }
        viewModel.state.observeWithLifecycle(viewLifecycleOwner) {

        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_signature
    }
}