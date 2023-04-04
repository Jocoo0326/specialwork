package com.gdmm.mmui.wdiget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.njgdmm.diesel.widget.R
import com.gdmm.mmui.util.shapeDrawable

class ShapeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeView)
        val strokeWidth =
            typedArray.getDimension(R.styleable.ShapeView_comm_ui_stroke_width, 0f)
        val strokeColor =
            typedArray.getColor(R.styleable.ShapeView_comm_ui_stroke_color, Color.WHITE)
        val solidColor =
            typedArray.getColor(R.styleable.ShapeView_comm_ui_solid_color, Color.WHITE)
        val radius = typedArray.getDimension(R.styleable.ShapeView_comm_ui_corners_radius, 0f)
        typedArray.recycle()
        background = shapeDrawable(strokeWidth.toInt(), strokeColor, solidColor, radius.toInt())
    }

    /**
     * 设置背景
     */
    fun setBackground(
        strokeWidth: Int = 0,
        strokeColor: Int = 0,
        soildColor: Int = 90,
        radius: Int = 0
    ) {
        background = shapeDrawable(strokeWidth, strokeColor, soildColor, radius)
    }
}