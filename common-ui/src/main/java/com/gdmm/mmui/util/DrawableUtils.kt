package com.gdmm.mmui.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import android.view.Gravity
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import com.njgdmm.diesel.widget.R
import top.defaults.drawabletoolbox.DrawableBuilder

/**
 * 渐变drawable
 *
 * @param startColor 开始颜色
 * @param endColor 结束颜色
 * @param cornerRadius 圆角
 * @param angle 渐变方向
 * @return drawable
 */
fun gradientDrawable(
    startColor: Int,
    endColor: Int,
    cornerRadius: Int = 0,
    angle: Int = 90,
    topLeftRadius: Int = 0,
    topRightRadius: Int = 0,
    bottomLeftRadius: Int = 0,
    bottomRightRadius: Int = 0
): Drawable {
    val builder = DrawableBuilder()
        .rectangle()
        .gradient()
        .linearGradient()
        .angle(angle)
        .startColor(startColor)
        .endColor(endColor)
    return if (cornerRadius > 0) {
        builder.cornerRadius(cornerRadius)
            .build()
    } else {
        builder.cornerRadii(topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
            .build()
    }
}

/**
 * Shape Drawable
 *
 * @param strokeWidth 边框厚度
 * @param strokeColor 边框颜色
 * @param cornerRadius 圆角大小
 * @param solidColor 填充颜色
 * @return Drawable
 */
fun shapeDrawable(
    strokeWidth: Int = 0,
    strokeColor: Int = 0,
    solidColor: Int = Color.TRANSPARENT,
    cornerRadius: Int = 0,
    topLeftRadius: Int = 0,
    topRightRadius: Int = 0,
    bottomLeftRadius: Int = 0,
    bottomRightRadius: Int = 0
): Drawable {
    val builder = DrawableBuilder()
        .rectangle()
        .solidColor(solidColor)
        .strokeWidth(strokeWidth)
        .strokeColor(strokeColor)
    return if (cornerRadius > 0) {
        builder.cornerRadius(cornerRadius).build()
    } else {
        builder.topLeftRadius(topLeftRadius)
            .topRightRadius(topRightRadius)
            .bottomLeftRadius(bottomLeftRadius)
            .bottomRightRadius(bottomRightRadius)
            .build()
    }
}

/**
 * 圆点
 */

fun solidRounderDrawable(
    solidColor: Int,
    width: Int
): Drawable {
    return DrawableBuilder()
        .oval()
        .width(width)
        .solidColor(solidColor)
        .build()
}

/**
 * stateListDrawable
 */
fun stateListDrawable(
    normal: Drawable,
    pressed: Drawable? = null,
    selected: Drawable? = null,
    checked: Drawable? = null,
    enabled: Drawable? = null,
    disabled: Drawable? = null
): Drawable {
    val stateListDrawable = StateListDrawable()

    pressed?.let {
        stateListDrawable.addState(
            intArrayOf(android.R.attr.state_pressed), it
        )
    }
    selected?.let {
        stateListDrawable.addState(
            intArrayOf(android.R.attr.state_selected), it
        )
    }

    checked?.let {
        stateListDrawable.addState(
            intArrayOf(android.R.attr.state_checked), it
        )
    }

    enabled?.let {
        stateListDrawable.addState(
            intArrayOf(android.R.attr.state_enabled), it
        )
    }

    disabled?.let {
        stateListDrawable.addState(
            intArrayOf(-android.R.attr.state_enabled), it
        )
    }

    stateListDrawable.addState(StateSet.WILD_CARD, normal)
    return stateListDrawable
}


fun Context.badgeDrawable(
    @DrawableRes drawableId: Int,
    unReadMsgNum: Int,
    @ColorRes badgeColor: Int = R.color.comm_ui_badge_color,
    @DimenRes badgeSize: Int = R.dimen.comm_ui_badge_size,
    gravity: Int = Gravity.TOP or Gravity.END,
    @Px margin: Float = 0.0f,
    @ColorRes textColor: Int = R.color.comm_ui_badge_text_color,
    @ColorRes badgeBorderColor: Int = R.color.comm_ui_badge_border_color,
    @DimenRes badgeBorderSize: Int = R.dimen.comm_ui_badge_border_size
): Drawable {

    return DrawableBadge.Builder(this)
        .drawableResId(drawableId)
        .badgeColor(badgeColor)
        .badgeSize(badgeSize)
        .badgeGravity(gravity)
        .badgeMargin(margin)
        .textColor(textColor)
        .showBorder(true)
        .badgeBorderColor(badgeBorderColor)
        .badgeBorderSize(badgeBorderSize)
        .maximumCounter(99)
        .showCounter(true)
        .build()
        .get(unReadMsgNum)
}