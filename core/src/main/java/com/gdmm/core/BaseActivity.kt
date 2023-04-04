package com.gdmm.core

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.gdmm.core.extensions.setStatusBar
import com.gdmm.core.util.DoubleClickExitDetector


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var progress: Dialog? = null

    protected lateinit var mBinding: VB
    private lateinit var doubleClickExitDetector: DoubleClickExitDetector

    open fun isDoubleClickExit(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = getViewBinding()
        setContentView(mBinding.root)
        doubleClickExitDetector = DoubleClickExitDetector(this)
        setStatusTheme()
    }

    open fun setStatusTheme() {
        setStatusBar(statusBarColor = Color.WHITE, isFullscreen = false)
    }

    open fun showLoading() {
        if (progress?.isShowing == true) {
            progress?.dismiss()
            progress = null
        }
        progress = MaterialProgressDialog(this).show()
    }

    abstract fun getViewBinding(): VB

    override fun onBackPressed() {
        if (isDoubleClickExit()) {
            val isExit = doubleClickExitDetector.click()
            if (isExit) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    open fun onLoadCompleted() {
        progress?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        progress?.dismiss()
    }
}