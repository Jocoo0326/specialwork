package com.gdmm.core.util

import android.content.Context
import android.graphics.Color
import android.view.View
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils

fun Context.tryAttachBadgeToAnchor(
    anchor: View,
    unReadNum: Int = 0,
    badgeTextColor: Int = Color.WHITE,
    badgeBgColor: Int = Color.RED,
    verticalOffsetWithText: Int = 0,
    horizontalOffsetWithText: Int = 0,
    badgeRadius: Int = 0,
    badgeWithTextRadius: Int = 0,
    badgeWidePadding: Int = 0
) {

    val drawable = BadgeDrawable.create(this).apply {
        maxCharacterCount = 3
        this.backgroundColor = badgeBgColor
        this.badgeTextColor = badgeTextColor
        this.verticalOffsetWithText = verticalOffsetWithText
        this.horizontalOffsetWithText = horizontalOffsetWithText

        if (unReadNum > 0) {
            number = unReadNum
        } else {
            clearNumber()
        }
    }

    if (badgeRadius > 0) {//没有设置数字的时候，圆点的半径
        val badgeRadiusField = BadgeDrawable::class.java.getDeclaredField("badgeRadius")
        badgeRadiusField.isAccessible = true
        badgeRadiusField.set(drawable, badgeRadius)
    }

    if (unReadNum > 0 && badgeWithTextRadius > 0) {//有数字，圆点的半径
        val badgeWithTextRadiusField = BadgeDrawable::class.java.getDeclaredField("badgeWithTextRadius")
        badgeWithTextRadiusField.isAccessible = true
        badgeWithTextRadiusField.set(drawable, badgeWithTextRadius)
    }

    if (unReadNum > 0 && badgeWidePadding > 0) {//有数字，数字超过9了，文字左右两边的间距
        val badgeWidePaddingField = BadgeDrawable::class.java.getDeclaredField("badgeWidePadding")
        badgeWidePaddingField.isAccessible = true
        badgeWidePaddingField.set(drawable, badgeWidePadding)
    }

    if (unReadNum > 0) {
        BadgeUtils.attachBadgeDrawable(drawable, anchor, null)
    } else {
        BadgeUtils.detachBadgeDrawable(drawable, anchor)
    }
}