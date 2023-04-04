package com.gdmm.mmui.wdiget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.view.isVisible
import com.njgdmm.diesel.widget.R

/**
 * 角标view
 * Created by Jocoo on 6/19/2017.
 */
class BadgeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val MIN_TEXT_SIZE = 6.0f
        private const val BORDER_WIDTH = 2
    }

    private var mRadius = 10
    var num = 9
    private var mShowBorder = true

    private val mBorderColor: Int
    private val bgColor: Int
    private val textColor: Int

    private val mPaint: Paint
    private var mText: String? = null
    private var offsetX = 0
    private var offsetY = 0
    private var textBound: Rect? = null

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.BadgeView)
        mRadius = array.getDimensionPixelSize(R.styleable.BadgeView_badgeRadius, mRadius)
        mShowBorder = array.getBoolean(R.styleable.BadgeView_showBorder, false)
        num = array.getInt(R.styleable.BadgeView_number, 0)
        mBorderColor = array.getColor(R.styleable.BadgeView_comm_ui_border_color, Color.WHITE)
        bgColor = array.getColor(R.styleable.BadgeView_comm_ui_bg_color, Color.RED)
        textColor = array.getColor(R.styleable.BadgeView_comm_ui_text_color, Color.WHITE)
        array.recycle()
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.textSize = MIN_TEXT_SIZE
        computeTextSize()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val h = 2 * mRadius + if (mShowBorder) 2 * BORDER_WIDTH else 0
        val w: Int = if (num > 99) {
            h + mRadius * 3 / 2 + paddingLeft + paddingRight
        } else if (num > 9) {
            h + mRadius + paddingLeft + paddingRight
        } else {
            h + paddingLeft + paddingRight
        }
        setMeasuredDimension(w, h)
    }

    private val numberText: Unit
        private get() {
            mText = if (num > 99) {
                "99+"
            } else if (num > 0) {
                num.toString()
            } else {
                ""
            }
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        computeTextSize()
        getTextDrawingOffset(w, h)
    }

    private fun getTextDrawingOffset(w: Int, h: Int) {
        numberText
        mPaint.getTextBounds(mText, 0, mText!!.length, textBound)
        offsetX = (w / 2 - textBound!!.exactCenterX()).toInt()
        offsetY = (h / 2 - textBound!!.exactCenterY()).toInt()
    }

    private fun computeTextSize() {
        var textSize = MIN_TEXT_SIZE
        //        float maxHeight = (float) (mRadius * 2.0f / Math.sqrt(2.0f));
//        float maxWidth = (float) (mRadius * 1.0f / Math.sqrt(2.0f));
        val maxHeight = mRadius * 2.0f - 2
        val maxWidth = (mRadius - 4).toFloat()
        textBound = Rect()
        val text = "9"
        mPaint.getTextBounds(text, 0, text.length, textBound)
        var lastWidth = textBound!!.width()
        var lastHeight = textBound!!.height()
        while (lastWidth <= maxWidth && lastHeight <= maxHeight) {
            textSize += 1.0f
            mPaint.textSize = textSize
            mPaint.getTextBounds(text, 0, text.length, textBound)
            lastWidth = textBound!!.width()
            lastHeight = textBound!!.height()
        }
        textSize -= 1.0f
        mPaint.textSize = textSize
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mShowBorder) {
            mPaint.color = mBorderColor
            canvas.drawRoundRect(
                RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat()),
                (mRadius + BORDER_WIDTH).toFloat(),
                (mRadius + BORDER_WIDTH).toFloat(),
                mPaint
            )
            mPaint.color = bgColor
            canvas.drawRoundRect(
                RectF(
                    BORDER_WIDTH.toFloat(),
                    BORDER_WIDTH.toFloat(),
                    (measuredWidth - BORDER_WIDTH).toFloat(),
                    (measuredHeight - BORDER_WIDTH).toFloat()
                ),
                mRadius.toFloat(), mRadius.toFloat(), mPaint
            )
        } else {
            mPaint.color = bgColor
            canvas.drawRoundRect(
                RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat()),
                mRadius.toFloat(), mRadius.toFloat(), mPaint
            )
        }
        mPaint.color = textColor
        canvas.drawText(mText!!, offsetX.toFloat(), offsetY.toFloat(), mPaint)
    }

    fun setNumber(number: Int) {
        isVisible = number > 0
        num = number
        getTextDrawingOffset(measuredWidth, measuredHeight)
        requestLayout()
        invalidate()
    }

    fun setRadius(radius: Int) {
        mRadius = context.dp2px(radius)
        invalidate()
    }
}

fun Context.dp2px(dp: Int): Int {
    return (this.resources.displayMetrics.density * dp).toInt()
}