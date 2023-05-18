package com.jocoo.swork.widget

import android.content.Context
import android.view.View
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.jocoo.swork.R
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.coroutines.flow.MutableSharedFlow

class CommonInputDialog(
    context: Context,
    private val title: String,
    private val flow: MutableSharedFlow<String>,
) : BottomPopupView(context) {

    override fun onCreate() {
        super.onCreate()
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = title
        val btnCancel = findViewById<View>(R.id.btn_cancel)
        val btnConfirm = findViewById<View>(R.id.btn_confirm)
        val etInput = findViewById<TextInputEditText>(R.id.et_input)
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnConfirm.setOnClickListener {
            val input = etInput.text.toString()
            flow.tryEmit(input)
            dismiss()
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_common_input
    }
}