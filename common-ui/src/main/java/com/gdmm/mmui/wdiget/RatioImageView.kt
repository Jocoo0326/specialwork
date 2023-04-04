package com.gdmm.mmui.wdiget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.njgdmm.diesel.widget.R

class RatioImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var aspectRatio = -1.0f

    init {
        var a: TypedArray? = null
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView, defStyleAttr, 0)
            aspectRatio = a.getFloat(R.styleable.RatioImageView_comm_ui_aspect_ratio, -1f)
        } finally {
            a?.recycle()
        }
    }

    fun setAspectRatio(aspectRatio: Float) {
        this.aspectRatio = aspectRatio
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (aspectRatio <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val height = (widthSize / aspectRatio).toInt()

        setMeasuredDimension(widthSize, height)
    }
}