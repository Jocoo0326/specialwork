package com.gdmm.mmui.wdiget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.util.ObjectsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.max

class TranslucentToolBarLayout @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(
        context, attributeSet, defStyleAttr
) {

    companion object {
        private const val STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height"
        private const val NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height"
    }

    private var lastInsets: WindowInsetsCompat? = null
    private var statusBarBackground: Drawable? = null
    private var _statusBarHeight = 0

    init {
        systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
                onApplyWindowInsets(insets)
            }
        }
        _statusBarHeight = getInternalDimensionSize(STATUS_BAR_HEIGHT_RES_NAME)
    }

    private fun getInternalDimensionSize(key: String): Int {
        val res = context.resources
        var result = 0
        val resourceId = res.getIdentifier(key, "dimen", "android")
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId)
        }
        return result
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var h = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            h = max(h, child.measuredHeight)
        }
        val th = lastInsets?.systemWindowInsetTop ?: _statusBarHeight
        val w = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(w, h + th)
    }

    private fun onApplyWindowInsets(insets: WindowInsetsCompat): WindowInsetsCompat {
        val newInsets: WindowInsetsCompat = insets
        if (!ObjectsCompat.equals(lastInsets, newInsets)) {
            lastInsets = newInsets
            setWillNotDraw(lastInsets == null || lastInsets!!.systemWindowInsetTop == 0)
            requestLayout()
        }
        return insets.consumeSystemWindowInsets()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (statusBarBackground != null) {
            var inset = lastInsets?.systemWindowInsetTop ?: 0
            if (inset == 0) {
                inset = _statusBarHeight
            }
            if (inset > 0) {
                statusBarBackground?.let {
                    it.setBounds(0, 0, width, inset)
                    it.draw(canvas)
                }
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val h = lastInsets?.systemWindowInsetTop ?: _statusBarHeight
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (!ViewCompat.getFitsSystemWindows(child)) {
                if (child.top < h) {
                    ViewCompat.offsetTopAndBottom(child, h)
                }
            }
        }
    }

    fun setStatusBarBackground(d: Drawable?) {
        if (d != statusBarBackground) {
            statusBarBackground?.let {
                it.callback = null
            }
            statusBarBackground = d?.mutate()
            statusBarBackground?.let {
                if (it.isStateful) {
                    it.state = drawableState
                }
                DrawableCompat.setLayoutDirection(it, ViewCompat.getLayoutDirection(this))
                it.setVisible(visibility == VISIBLE, false)
                it.callback = this
            }
            setWillNotDraw(false)
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    fun setStatusBarBackground(@DrawableRes d: Int) {
        setStatusBarBackground(ContextCompat.getDrawable(context, d))
    }
}