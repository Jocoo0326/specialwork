package com.jocoo.swork.widget

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.gdmm.core.extensions.observeWithLifecycle
import com.jocoo.swork.R
import com.jocoo.swork.util.toByteArray
import com.kyanogen.signatureview.SignatureView
import com.lxj.xpopup.core.BottomPopupView

class SignatureDialog(
    context: Context,
    private val actViewModel: UploadImageViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val callback: ((String) -> Unit)? = null
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
            actViewModel.uploadImage(signatureView.signatureBitmap.toByteArray())
        }
        actViewModel.uploadImageFlow.observeWithLifecycle(viewLifecycleOwner) {
            callback?.invoke(it)
            dismiss()
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_signature
    }
}