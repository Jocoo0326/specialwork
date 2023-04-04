package com.gdmm.mmui.wdiget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.njgdmm.diesel.widget.R

class RoundImageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attributeSet, defStyleAttr) {

    private var mCornerRadius: Int
    private var mTopLeftRadius: Int
    private var mTopRightRadius: Int
    private var mBottomLeftRadius: Int
    private var mBottomRightRadius: Int
    private var mStrokeWidth: Int
    private var mStrokeColor: Int
    private var mPath: Path = Path()
    private val mStrokePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).also {
            it.style = Paint.Style.STROKE
        }
    }

    init {
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.CusRoundImageView)
        mCornerRadius = typedArray.getDimensionPixelSize(
            R.styleable.CusRoundImageView_comm_ui_corners_radius,
            0
        )
        mTopLeftRadius = typedArray.getDimensionPixelSize(
            R.styleable.CusRoundImageView_comm_ui_top_left_radius,
            0
        )
        mTopRightRadius = typedArray.getDimensionPixelSize(
            R.styleable.CusRoundImageView_comm_ui_top_right_radius,
            0
        )
        mBottomLeftRadius = typedArray.getDimensionPixelSize(
            R.styleable.CusRoundImageView_comm_ui_bottom_left_radius,
            0
        )
        mBottomRightRadius = typedArray.getDimensionPixelSize(
            R.styleable.CusRoundImageView_comm_ui_bottom_right_radius,
            0
        )
        mStrokeWidth = typedArray.getDimensionPixelSize(
            R.styleable.CusRoundImageView_comm_ui_stroke_width,
            0
        )
        mStrokeColor = typedArray.getColor(
            R.styleable.CusRoundImageView_comm_ui_stroke_color,
            0
        )
        typedArray.recycle()
    }

    private fun hasCornerSet(): Boolean {
        return !(mCornerRadius == 0 && mTopLeftRadius == 0 && mTopRightRadius == 0 &&
                mBottomLeftRadius == 0 && mBottomRightRadius == 0)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (!hasCornerSet()) return

        val rect = RectF(
            paddingLeft.toFloat(),
            paddingTop.toFloat(), (w - paddingRight).toFloat(), (h - paddingBottom).toFloat()
        )
        if (mStrokeWidth > 0) {
            val inset = mStrokeWidth * 0.5f
            rect.inset(inset, inset)
        }

        val tlr = if (mCornerRadius > 0) mCornerRadius else mTopLeftRadius
        val trr = if (mCornerRadius > 0) mCornerRadius else mTopRightRadius
        val blr = if (mCornerRadius > 0) mCornerRadius else mBottomLeftRadius
        val brr = if (mCornerRadius > 0) mCornerRadius else mBottomRightRadius

        mPath.addRoundRect(
            rect,
            floatArrayOf(
                tlr.toFloat(),
                tlr.toFloat(),
                trr.toFloat(),
                trr.toFloat(),
                brr.toFloat(),
                brr.toFloat(),
                blr.toFloat(),
                blr.toFloat()
            ),
            Path.Direction.CW
        )
    }

    override fun draw(canvas: Canvas?) {
        canvas?.save()
        if (hasCornerSet()) {
            canvas?.clipPath(mPath)
        }
        super.draw(canvas)
        canvas?.restore()
        if (mStrokeWidth > 0) {
            mStrokePaint.strokeWidth = mStrokeWidth.toFloat()
            mStrokePaint.color = mStrokeColor
            canvas?.drawPath(mPath, mStrokePaint)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}