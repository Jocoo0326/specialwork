package com.gdmm.core.extensions

import androidx.annotation.NonNull
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * show BottomSheetDialogFragment(防止快速点击报错或弹多个)
 */
fun BottomSheetDialogFragment.safeShow(
    @NonNull manager: FragmentManager, tag: String, callback: (() -> Unit)? = null
) {
    if (!isAdded && manager.findFragmentByTag(tag) == null) {
        show(manager, tag)
        callback?.invoke()
    }
}