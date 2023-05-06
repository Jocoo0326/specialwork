package com.jocoo.swork.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.gdmm.mmui.wdiget.dp2px
import com.jocoo.swork.R
import com.jocoo.swork.databinding.LayoutSegmentItemBinding

class SegmentItem constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var hasBottomLine: Boolean
    private var hasTopLine: Boolean
    private var editable: Boolean
    private var arrow: Drawable?
    private var mPaddingBottom: Int
    private var mPaddingRight: Int
    private var mPaddingTop: Int
    private var mPaddingLeft: Int
    private var rightTextGravity: Int
    private var rightImage: Drawable?
    private var leftImage: Drawable?
    private var rightImageWidth: Int
    private var leftImageWidth: Int
    private var leftTextWidth: Int
    private var rightTextSize: Float
    private var leftTextSize: Float
    private var rightTextHintColor: Int
    private var leftTextHintColor: Int
    private var leftTextColor: Int
    private var rightTextColor: Int
    private var rightTextHint: String?
    private var leftTextHint: String?
    private var rightText: String?
    private var leftText: String?
    private var mBinding: LayoutSegmentItemBinding
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mBinding = LayoutSegmentItemBinding.inflate(LayoutInflater.from(context), this)
        val typedArray = context.obtainStyledAttributes(
            attributeSet, R.styleable.SegmentItem, defStyleAttr, R.style.SegmentItem_Default
        )
        leftText = typedArray.getString(R.styleable.SegmentItem_segmentLeftText)
        rightText = typedArray.getString(R.styleable.SegmentItem_segmentRightText)
        leftTextHint = typedArray.getString(R.styleable.SegmentItem_segmentLeftTextHint)
        rightTextHint = typedArray.getString(R.styleable.SegmentItem_segmentRightTextHint)
        leftTextColor =
            typedArray.getColor(R.styleable.SegmentItem_segmentLeftTextColor, Color.BLACK)
        leftTextHintColor =
            typedArray.getColor(
                R.styleable.SegmentItem_segmentLeftTextHintColor,
                0xFF333333.toInt()
            )
        rightTextColor =
            typedArray.getColor(R.styleable.SegmentItem_segmentRightTextColor, Color.BLACK)
        rightTextHintColor =
            typedArray.getColor(
                R.styleable.SegmentItem_segmentRightTextHintColor,
                0xFF333333.toInt()
            )
        leftTextSize =
            typedArray.getDimension(
                R.styleable.SegmentItem_segmentLeftTextSize,
                context.dp2px(18).toFloat()
            )
        rightTextSize =
            typedArray.getDimension(
                R.styleable.SegmentItem_segmentRightTextSize,
                context.dp2px(16).toFloat()
            )
        leftTextWidth =
            typedArray.getDimensionPixelSize(R.styleable.SegmentItem_segmentLeftTextWidth, 0)
        leftImageWidth =
            typedArray.getDimensionPixelSize(R.styleable.SegmentItem_segmentLeftImageWidth, 0)
        rightImageWidth =
            typedArray.getDimensionPixelSize(R.styleable.SegmentItem_segmentRightImageWidth, 0)
        leftImage = typedArray.getDrawable(R.styleable.SegmentItem_segmentLeftImage)
        rightImage = typedArray.getDrawable(R.styleable.SegmentItem_segmentRightImage)
        rightTextGravity = typedArray.getInt(R.styleable.SegmentItem_segmentRightTextGravity, 5)
        mPaddingLeft =
            typedArray.getDimensionPixelOffset(R.styleable.SegmentItem_segmentPaddingLeft, 0)
        mPaddingTop =
            typedArray.getDimensionPixelOffset(R.styleable.SegmentItem_segmentPaddingTop, 0)
        mPaddingRight =
            typedArray.getDimensionPixelOffset(R.styleable.SegmentItem_segmentPaddingRight, 0)
        mPaddingBottom =
            typedArray.getDimensionPixelOffset(R.styleable.SegmentItem_segmentPaddingBottom, 0)
        arrow = typedArray.getDrawable(R.styleable.SegmentItem_segmentArrow)
        editable = typedArray.getBoolean(R.styleable.SegmentItem_segmentEditable, false)
        hasTopLine = typedArray.getBoolean(R.styleable.SegmentItem_segmentHasTopLine, false)
        hasBottomLine = typedArray.getBoolean(R.styleable.SegmentItem_segmentHasBottomLine, false)
        typedArray.recycle()
        setupRoot()
        setLeftImage(leftImage, leftImageWidth)
        setRightImage(rightImage, rightImageWidth)
        setLeftText(
            leftText,
            leftTextWidth,
            leftTextHint,
            leftTextSize,
            leftTextColor,
            leftTextHintColor
        )
        setRightText(
            rightText,
            rightTextHint,
            rightTextSize,
            rightTextColor,
            rightTextHintColor,
            rightTextGravity
        )
        setArrow(arrow)
    }

    fun setArrow(arrow: Drawable?) {
        mBinding.arrow.visibility = if (arrow == null) View.GONE else View.VISIBLE
        mBinding.arrow.setImageDrawable(arrow)
    }

    fun setRightText(
        rightText: String?,
        rightTextHint: String?,
        rightTextSize: Float,
        rightTextColor: Int,
        rightTextHintColor: Int,
        rightTextGravity: Int
    ) {
        if (editable) {
            mBinding.rightEditText.apply {
                setText(rightText)
                hint = rightTextHint
                setHintTextColor(rightTextHintColor)
                textSize = rightTextSize
                setTextColor(rightTextColor)
                gravity = rightTextGravity
            }
        } else {
            mBinding.rightText.apply {
                text = rightText
                hint = rightTextHint
                textSize = rightTextSize
                setTextColor(rightTextColor)
                gravity = rightTextGravity
            }
        }
    }

    fun setLeftText(
        leftText: String?,
        leftTextWidth: Int,
        leftTextHint: String?,
        leftTextSize: Float,
        leftTextColor: Int,
        leftTextHintColor: Int
    ) {
        mBinding.leftText.apply {
            text = leftText
            if (leftTextWidth > 0) {
                width = leftTextWidth
            }
            hint = leftTextHint
            setHintTextColor(leftTextHintColor)
            textSize = leftTextSize
            setTextColor(leftTextColor)
        }
    }

    fun setLeftImage(leftImage: Drawable?, leftImageWidth: Int) {
        if (leftImageWidth <= 0) {
            mBinding.leftImage.visibility = View.GONE
        } else {
            mBinding.leftImage.apply {
                visibility = View.VISIBLE
                setImageDrawable(leftImage)
                val lp = layoutParams
                lp.width = leftImageWidth
                layoutParams = lp
            }
        }
    }

    fun setRightImage(rightImage: Drawable?, rightImageWidth: Int) {
        if (rightImageWidth <= 0) {
            mBinding.rightImage.visibility = View.GONE
        } else {
            mBinding.rightImage.apply {
                visibility = View.VISIBLE
                val lp = layoutParams
                lp.width = rightImageWidth
                layoutParams = lp
            }
        }
    }

    private fun setupRoot() {
        mBinding.apply {
            layoutSegmentItem.isClickable = isClickable
            layoutSegmentItem.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
            if (editable) {
                rightText.visibility = View.GONE
                rightEditText.visibility = View.VISIBLE
            } else {
                rightText.visibility = View.VISIBLE
                rightEditText.visibility = View.GONE
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val lineWidth = 1.0f
        mPaint.reset()
        mPaint.color = 0xFFE0E0E0.toInt()
        mPaint.strokeWidth = lineWidth
        if (hasTopLine) {
            canvas?.drawLine(0f, 0f, measuredWidth.toFloat(), 0f, mPaint)
        }
        if (hasBottomLine) {
            canvas?.drawLine(
                0f,
                (measuredHeight - lineWidth),
                measuredWidth.toFloat(),
                measuredHeight.toFloat(),
                mPaint
            )
        }
    }
}