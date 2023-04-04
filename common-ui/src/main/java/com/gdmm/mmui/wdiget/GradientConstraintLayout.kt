package com.gdmm.mmui.wdiget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.njgdmm.diesel.widget.R
import com.gdmm.mmui.util.gradientDrawable

/**
 * 支持渐变背景的 ConstraintLayout
 *
 * @constructor 构造
 *
 * @param context context
 * @param attrs attrs
 * @param defStyleAttr
 */
open class GradientConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.GradientConstraintLayout,
            defStyleAttr,
            0
        )
        val startColor = typedArray.getColor(
            R.styleable.GradientConstraintLayout_comm_ui_startcolor,
            Color.TRANSPARENT
        )
        val endColor = typedArray.getColor(
            R.styleable.GradientConstraintLayout_comm_ui_endcolor,
            Color.TRANSPARENT
        )

        val angle = typedArray.getInt(R.styleable.GradientConstraintLayout_comm_ui_angle, 90)
        val radius =
            typedArray.getDimensionPixelSize(R.styleable.GradientConstraintLayout_comm_ui_corners_radius, 0)
        typedArray.recycle()

        background = gradientDrawable(startColor, endColor, radius, angle)
    }

    /**
     * 设置背景
     *
     * @param startColor 开始颜色
     * @param endColor 结束颜色
     * @param angle 渐变方向
     * @param radius 圆角
     */
    fun setBackground(startColor: Int, endColor: Int, angle: Int = 90, radius: Int = 0) {
        background = gradientDrawable(startColor, endColor, radius, angle)
    }
}