package com.njgdmm.diesel.widget.tag

import android.content.Context
import android.database.DataSetObserver
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ListAdapter

/**
 * 流式布局 标签
 *
 * @constructor
 *
 * @param context
 * @param attributeSet
 * @param defStyleAttr
 */
class TagViewGroup @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr) {
    
    /**
     * measure child view
     *
     * @param child
     * @param parentWidthMeasureSpec
     * @param parentHeightMeasureSpec
     */
    private fun measureChildView(
        child: View,
        parentWidthMeasureSpec: Int,
        parentHeightMeasureSpec: Int
    ): MarginLayoutParams {
        val lp = child.layoutParams as MarginLayoutParams
        val childWidthMeasureSpec =
            getChildMeasureSpec(parentWidthMeasureSpec, lp.marginStart + lp.marginEnd, lp.width)
        val childHeightMeasureSpec =
            getChildMeasureSpec(parentHeightMeasureSpec, lp.topMargin + lp.bottomMargin, lp.height)
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
        return lp
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = getDefaultSize(0, widthMeasureSpec)
        var heightSize = 0
        
        val verticalPadding = paddingTop + paddingBottom
        val horizontalPadding = paddingStart + paddingEnd
        
        var multipleChildWidthSum = horizontalPadding
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = measureChildView(child, widthMeasureSpec, heightMeasureSpec)
            
            val childWidth = child.measuredWidth + lp.marginStart + lp.marginEnd
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
            
            if (multipleChildWidthSum + childWidth > widthSize) {//需要换行
                //换行之后,下一行已使用的宽度更改为初始值,高度累加
                multipleChildWidthSum = horizontalPadding + childWidth
                heightSize += childHeight
            } else {
                //同一行高度不变,宽度累加
                heightSize = heightSize.coerceAtLeast(childHeight)
                multipleChildWidthSum += childWidth;
            }
        }
        heightSize += verticalPadding
        setMeasuredDimension(widthSize, heightSize)
    }
    
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width = r - l
        var left = paddingStart
        var top = paddingTop
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as MarginLayoutParams
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            
            child.layout(left, top, left + childWidth, top + childHeight)
            
            if (left + childWidth + lp.marginStart + lp.marginEnd + paddingEnd > width) {//换行
                left = paddingStart + childWidth
                top += childHeight + lp.topMargin + lp.bottomMargin
            } else {
                left += childWidth + lp.marginStart + lp.marginEnd
            }
        }
    }
    
    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p != null && p is LayoutParams
    }
    
    override fun generateDefaultLayoutParams() = LayoutParams(MATCH_PARENT, MATCH_PARENT)
    
    override fun generateLayoutParams(p: ViewGroup.LayoutParams?) = LayoutParams(p)
    
    override fun generateLayoutParams(attrs: AttributeSet?) = LayoutParams(context, attrs)
    
    class LayoutParams : MarginLayoutParams {
        internal constructor(c: Context?, attrs: AttributeSet?) : super(c, attrs)
        internal constructor(width: Int, height: Int) : super(width, height)
        internal constructor(source: MarginLayoutParams?) : super(source)
        internal constructor(source: ViewGroup.LayoutParams?) : super(source)
    }
    
    
    private val mDataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            setupChildren()
        }
        
        override fun onInvalidated() {
            setupChildren()
        }
    }
    
    private fun setupChildren() {
        mAdapter?.let { adapter ->
            removeAllViews()
            for (i in 0 until adapter.count) {
                val child = mAdapter!!.getView(i, null, this)
                child.setOnClickListener {
                    mOnItemClickListener?.onItemClick(this, child, i, adapter.getItemId(i))
                }
                val lp = child.layoutParams
                if (lp != null) {
                    addViewInLayout(child, -1, lp, true)
                } else {
                    addView(child, -1)
                }
            }
        }
    }
    
    fun setAdapter(adapter: ListAdapter) {
        mAdapter?.unregisterDataSetObserver(mDataSetObserver)
        mAdapter = adapter
        mAdapter?.registerDataSetObserver(mDataSetObserver)
        setupChildren()
    }
    
    private var mAdapter: ListAdapter? = null
    
    var mOnItemClickListener: OnItemClickListener? = null
    
    interface OnItemClickListener {
        fun onItemClick(parent: TagViewGroup, view: View?, position: Int, id: Long)
    }
}