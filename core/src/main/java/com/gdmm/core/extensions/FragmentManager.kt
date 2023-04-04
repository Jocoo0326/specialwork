package com.gdmm.core.extensions

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * 提交事物
 *
 * @param allowStateLoss 默认值true
 * @param func
 */
inline fun FragmentManager.commitTransaction(
    allowStateLoss: Boolean = true,
    func: FragmentTransaction.() -> FragmentTransaction
) {
    val transaction = beginTransaction().func()
    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

