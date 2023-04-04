@file:JvmName("ToastUtil")

package com.gdmm.mmui.util

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.njgdmm.diesel.widget.databinding.CommUiLayoutSuccessToastBinding
import java.lang.ref.WeakReference


/**
 * show custom toast
 */
fun Context.showCustomToast(text: CharSequence) {
    val mReference = WeakReference(this)
    if (mReference.get() == null) return

    val toast = Toast(mReference.get())
    val mBind = CommUiLayoutSuccessToastBinding.inflate(LayoutInflater.from(mReference.get()))
    mBind.message.apply {
        this.text = text
        val drawable = shapeDrawable(
            strokeWidth = 0,
            solidColor = Color.parseColor("#CC000000"),
            cornerRadius = 16
        )
        background = drawable
    }


    toast.apply {
        view = mBind.root
        duration = Toast.LENGTH_SHORT
        setGravity(Gravity.CENTER, 0, 0)
    }.show()
}



