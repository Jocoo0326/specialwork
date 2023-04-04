package com.gdmm.mmui.wdiget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.njgdmm.diesel.widget.R
import com.gdmm.mmui.util.gradientDrawable

/**
 * 支持渐变背景的TextView
 *
 * @constructor
 * @param context
 * @param attrs
 * @param defStyleAttr
 */
class GradientTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView)

        val startColor =
            typedArray.getColor(R.styleable.GradientTextView_comm_ui_startcolor, Color.TRANSPARENT)
        val endColor =
            typedArray.getColor(R.styleable.GradientTextView_comm_ui_endcolor, Color.TRANSPARENT)
        val angle = typedArray.getInt(R.styleable.GradientTextView_comm_ui_angle, 90)
        val cornerRadius =
            typedArray.getDimensionPixelSize(R.styleable.GradientTextView_comm_ui_corners_radius, 0)
        val topLeftRadius =
            typedArray.getDimension(R.styleable.GradientTextView_comm_ui_top_left_radius, 0f)
        val topRightRadius =
            typedArray.getDimension(R.styleable.GradientTextView_comm_ui_top_right_radius, 0f)
        val bottomLeftRadius =
            typedArray.getDimension(R.styleable.GradientTextView_comm_ui_bottom_left_radius, 0f)
        val bottomRightRadius =
            typedArray.getDimension(R.styleable.GradientTextView_comm_ui_bottom_right_radius, 0f)
        typedArray.recycle()

        background = gradientDrawable(
            startColor, endColor, cornerRadius, angle,
            topLeftRadius.toInt(),
            topRightRadius.toInt(),
            bottomLeftRadius.toInt(),
            bottomRightRadius.toInt()
        )
    }

    /**
     * 设置背景
     *
     * @param startColor 开始颜色
     * @param endColor 结束颜色
     * @param angle 渐变方向
     * @param radius 圆角
     */
    fun setBackground(startColor: Int, endColor: Int, angle: Int = 0, radius: Int = 0) {
        background = gradientDrawable(startColor, endColor, radius, angle)
    }

}