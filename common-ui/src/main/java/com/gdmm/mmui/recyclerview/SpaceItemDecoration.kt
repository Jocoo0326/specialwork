package com.gdmm.mmui.recyclerview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(
    private val space: Int,
    private val showLeadingSpace: Boolean = false,
    private val showEndingSpace: Boolean = true,
    @ColorRes private val spacingColor: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val childAdapterPosition = parent.getChildAdapterPosition(view)
        val lm = parent.layoutManager
        val count = parent.adapter!!.itemCount
        if (lm !is GridLayoutManager && lm is LinearLayoutManager) {
            if (lm.orientation == RecyclerView.VERTICAL) {
                if ((childAdapterPosition > 0) || (showLeadingSpace && childAdapterPosition == 0)) {
                    outRect.top = space
                }
                if (childAdapterPosition == parent.adapter!!.itemCount - 1 && showEndingSpace) {
                    outRect.bottom = space
                }
            } else {
                if ((childAdapterPosition > 0) || (showLeadingSpace && childAdapterPosition == 0)) {
                    outRect.left = space
                }
                if (childAdapterPosition == parent.adapter!!.itemCount - 1 && showEndingSpace) {
                    outRect.right = space
                }
            }
        } else if (lm is GridLayoutManager) {
            val sc = lm.spanCount
            val i = childAdapterPosition % sc
            if (space < 3) {
                outRect.left = if (i > 0) space else 0
            } else {
                when (i) {
                    0 -> {
                        outRect.left = 0
                        outRect.right = space * 2 / 3
                    }
                    sc - 1 -> {
                        outRect.right = 0
                        outRect.left = space * 2 / 3
                    }
                    else -> {
                        outRect.left = space / 3
                        outRect.right = space / 3
                    }
                }
            }

            if (childAdapterPosition >= sc || showLeadingSpace) {
                outRect.top = space
            }
            if (showEndingSpace && childAdapterPosition >= ((count - 1) / sc) * sc) {
                outRect.bottom = space
            }
        } else {
            super.getItemOffsets(outRect, view, parent, state)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (spacingColor == 0) return
        val spaceColorInt = ContextCompat.getColor(parent.context, spacingColor)
        val p = Paint()
        p.color = spaceColorInt
        val lm = parent.layoutManager
        val count = parent.childCount
        if (lm !is GridLayoutManager && lm is LinearLayoutManager) {
            if (lm.orientation == RecyclerView.VERTICAL) {
                for (i in 0 until count) {
                    val childView = parent.getChildAt(i)
                    val childAdapterPosition = parent.getChildAdapterPosition(childView)
                    if ((childAdapterPosition > 0) || (showLeadingSpace && childAdapterPosition == 0)) {
                        drawTop(c, p, parent, childView)
                    }
                    if (childAdapterPosition == parent.adapter!!.itemCount - 1 && showEndingSpace) {
                        drawBottom(c, p, parent, childView)
                    }
                }
            } else {
                for (i in 0 until count) {
                    val childView = parent.getChildAt(i)
                    val childAdapterPosition = parent.getChildAdapterPosition(childView)
                    if ((childAdapterPosition > 0) || (showLeadingSpace && childAdapterPosition == 0)) {
                        drawLeft(c, p, parent, childView)
                    }
                    if (childAdapterPosition == parent.adapter!!.itemCount - 1 && showEndingSpace) {
                        drawRight(c, p, parent, childView)
                    }
                }
            }
        } else if (lm is GridLayoutManager) {
            val sc = lm.spanCount
            for (i in 0 until count) {
                val childView = parent.getChildAt(i)
                val childAdapterPosition = parent.getChildAdapterPosition(childView)
                val j = childAdapterPosition % sc
                if (space < 3) {
                    if (j > 0) {
                        drawLeft(c, p, parent, childView)
                    }
                } else {
                    when (j) {
                        0 -> {
                            drawRight(c, p, parent, childView, (space * 2 / 3).toFloat())
                        }
                        sc - 1 -> {
                            drawLeft(c, p, parent, childView, (space * 2 / 3).toFloat())
                        }
                        else -> {
                            drawLeft(c, p, parent, childView, (space / 3).toFloat())
                            drawRight(c, p, parent, childView, (space / 3).toFloat())
                        }
                    }
                }

                if (childAdapterPosition >= sc || showLeadingSpace) {
                    drawTop(c, p, parent, childView)
                }
                if (showEndingSpace && childAdapterPosition >= ((count-1) / sc) * sc) {
                    drawBottom(c, p, parent, childView)
                }
            }
        }
    }

    private fun drawTop(
        c: Canvas,
        p: Paint,
        parent: RecyclerView,
        childView: View,
        d: Float = space.toFloat()
    ) {
        c.drawRect(
            0f,
            childView.top - d,
            parent.width.toFloat(),
            childView.top.toFloat(),
            p
        )
    }

    private fun drawLeft(
        c: Canvas,
        p: Paint,
        parent: RecyclerView,
        childView: View,
        d: Float = space.toFloat()
    ) {
        c.drawRect(
            childView.left - d,
            0f,
            childView.left.toFloat(),
            parent.height.toFloat(),
            p
        )
    }

    private fun drawRight(
        c: Canvas,
        p: Paint,
        parent: RecyclerView,
        childView: View,
        d: Float = space.toFloat()
    ) {
        c.drawRect(
            childView.right.toFloat(),
            0f,
            childView.right - d,
            parent.height.toFloat(),
            p
        )
    }

    private fun drawBottom(
        c: Canvas,
        p: Paint,
        parent: RecyclerView,
        childView: View,
        d: Float = space.toFloat()
    ) {
        c.drawRect(
            0f,
            childView.bottom.toFloat(),
            parent.width.toFloat(),
            childView.bottom + d,
            p
        )
    }
}