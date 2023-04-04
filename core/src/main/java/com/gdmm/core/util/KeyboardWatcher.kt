package com.gdmm.core.util

import android.graphics.Rect
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import java.lang.ref.WeakReference


class KeyboardWatcher(
    rootLayout: ViewGroup,
    private val onKeyboardHidden: (() -> Unit)? = null,
    private val onKeyboardShown: ((keyboardSize: Int) -> Unit)? = null
) {
    
    private var rootLayoutRef: WeakReference<ViewGroup>
    private var globalLayoutListener: OnGlobalLayoutListener? = null
    
    companion object {
        const val MIN_HEIGHT = 150
    }
    
    init {
        rootLayoutRef = WeakReference(rootLayout)
        registerGlobalLayoutListener()
    }
    
    private fun registerGlobalLayoutListener() {
        var isShow = false
        globalLayoutListener = OnGlobalLayoutListener {
            val r = Rect()
            //r will be populated with the coordinates of your view that area still visible.
            rootLayoutRef.get()?.getWindowVisibleDisplayFrame(r)
            
            val heightDiff: Int = (rootLayoutRef.get()?.rootView?.height ?: 0) - (r.bottom - r.top)
            if (heightDiff > MIN_HEIGHT) { // if more than 100 pixels, its probably a keyboard...
                isShow = true
                onKeyboardShown?.invoke(heightDiff)
            } else {
                if (isShow) {
                    onKeyboardHidden?.invoke()
                    isShow = false
                }
            }
        }
        rootLayoutRef.get()?.viewTreeObserver?.addOnGlobalLayoutListener(globalLayoutListener)
    }
    
    fun unregister() {
        globalLayoutListener?.let {
            rootLayoutRef.get()?.viewTreeObserver?.removeOnGlobalLayoutListener(it)
        }
    }
    
}