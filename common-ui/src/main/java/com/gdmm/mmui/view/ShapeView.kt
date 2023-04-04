package com.gdmm.mmui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.njgdmm.diesel.widget.R
import com.gdmm.mmui.util.shapeDrawable

class ShapeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeView)
        val strokeWidth =
            typedArray.getDimension(R.styleable.ShapeView_comm_ui_stroke_width, 0f)
        val strokeColor =
            typedArray.getColor(R.styleable.ShapeView_comm_ui_stroke_color, Color.WHITE)
        val solidColor =
            typedArray.getColor(R.styleable.ShapeView_comm_ui_solid_color, Color.WHITE)
        val radius = typedArray.getDimension(R.styleable.ShapeView_comm_ui_corners_radius, 0f)
        val topLeftRadius =
            typedArray.getDimension(R.styleable.ShapeView_comm_ui_top_left_radius, 0f)
        val topRightRadius =
            typedArray.getDimension(R.styleable.ShapeView_comm_ui_top_right_radius, 0f)
        val bottomLeftRadius =
            typedArray.getDimension(R.styleable.ShapeView_comm_ui_bottom_left_radius, 0f)
        val bottomRightRadius =
            typedArray.getDimension(R.styleable.ShapeView_comm_ui_bottom_right_radius, 0f)
        typedArray.recycle()
        background = shapeDrawable(
            strokeWidth.toInt(),
            strokeColor,
            solidColor,
            radius.toInt(),
            topLeftRadius.toInt(),
            topRightRadius.toInt(),
            bottomLeftRadius.toInt(),
            bottomRightRadius.toInt()
        )
    }
}