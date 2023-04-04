/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gdmm.mmui.recyclerview

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ShapeDrawable
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class InsetDividerItemDecoration @JvmOverloads constructor(
    @ColorInt private val color: Int = 0xffe1e1e1.toInt(),
    @Px val dividerThickness: Int = 1,
    @Px val dividerInsetStart: Int = 0,
    @Px val dividerInsetEnd: Int = 0,
    private val showFirstDivider: Boolean = false,
    private val showLastDivider: Boolean = false,
    val orientation: Int = LinearLayoutManager.VERTICAL,
) : RecyclerView.ItemDecoration() {

    private val dividerDrawable = DrawableCompat.wrap(ShapeDrawable())

    private val tempRect = Rect()

    init {
        DrawableCompat.setTint(dividerDrawable, color)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null) {
            return
        }
        if (orientation == VERTICAL) {
            drawForVerticalOrientation(canvas, parent)
        } else {
            drawForHorizontalOrientation(canvas, parent)
        }
    }

    private fun drawForVerticalOrientation(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        var left: Int
        var right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }
        val isRtl = ViewCompat.getLayoutDirection(parent) == ViewCompat.LAYOUT_DIRECTION_RTL
        left += if (isRtl) dividerInsetEnd else dividerInsetStart
        right -= if (isRtl) dividerInsetStart else dividerInsetEnd
        val childCount = if (showLastDivider) parent.childCount else parent.childCount - 1
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, tempRect)
            val bottom = tempRect.bottom + child.translationY.roundToInt()
            val top = bottom - dividerDrawable.intrinsicHeight - dividerThickness
            if (i == 0 && showFirstDivider) {
                dividerDrawable.setBounds(left, top, right, dividerDrawable.intrinsicHeight + dividerThickness)
                dividerDrawable.draw(canvas)
            }
            dividerDrawable.setBounds(left, top, right, bottom)
            dividerDrawable.draw(canvas)
        }
        canvas.restore()
    }

    private fun drawForHorizontalOrientation(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        var top: Int
        var bottom: Int
        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(
                parent.paddingLeft, top, parent.width - parent.paddingRight, bottom
            )
        } else {
            top = 0
            bottom = parent.height
        }
        top += dividerInsetStart
        bottom -= dividerInsetEnd
        val childCount = if (showLastDivider) parent.childCount else parent.childCount - 1
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.layoutManager!!.getDecoratedBoundsWithMargins(child, tempRect)
            if (i == 0 && showFirstDivider) {
                dividerDrawable.setBounds(tempRect.left, top, dividerDrawable.intrinsicWidth + dividerThickness, bottom)
            }
            val right = tempRect.right + child.translationX.roundToInt()
            val left = right - dividerDrawable.intrinsicWidth - dividerThickness
            dividerDrawable.setBounds(left, top, right, bottom)
            dividerDrawable.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect[0, 0, 0] = 0
        if (orientation == VERTICAL) {
            outRect.bottom = dividerDrawable.intrinsicHeight + dividerThickness
        } else {
            outRect.right = dividerDrawable.intrinsicWidth + dividerThickness
        }
    }

    companion object {
        const val VERTICAL = LinearLayout.VERTICAL
    }
}