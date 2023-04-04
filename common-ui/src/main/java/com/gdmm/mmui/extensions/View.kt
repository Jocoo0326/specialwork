package com.gdmm.mmui.extensions

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.gdmm.mmui.util.gradientDrawable
import com.gdmm.mmui.util.shapeDrawable
import com.gdmm.mmui.util.stateListDrawable

/**
 * 设置渐变背景
 *
 * @param startColor 开始颜色
 * @param endColor 结束颜色
 * @param cornerRadius 圆角
 * @param angle 渐变方向
 * @param topLeftRadius 左上角圆角
 * @param topRightRadius 右上角圆角
 * @param bottomLeftRadius 左下角圆角
 * @param bottomRightRadius 右下角圆角
 */
fun View.gradientBackground(
    startColor: Int = Color.TRANSPARENT,
    endColor: Int = Color.TRANSPARENT,
    cornerRadius: Int = 0,
    angle: Int = 90,
    topLeftRadius: Int = 0,
    topRightRadius: Int = 0,
    bottomLeftRadius: Int = 0,
    bottomRightRadius: Int = 0
) {
    val drawable = gradientDrawable(
        startColor = startColor,
        endColor = endColor,
        cornerRadius = cornerRadius,
        angle = angle,
        topLeftRadius = topLeftRadius,
        topRightRadius = topRightRadius,
        bottomLeftRadius = bottomLeftRadius,
        bottomRightRadius = bottomRightRadius
    )
    background = drawable
}

/**
 * 设置View背景
 *
 * @param strokeWidth 边框厚度
 * @param strokeColor 边框颜色
 * @param solidColor 填充颜色
 * @param cornerRadius 圆角大小
 * @param topLeftRadius 左上角圆角
 * @param topRightRadius 右上角圆角
 * @param bottomLeftRadius 左下角圆角
 * @param bottomRightRadius 右下角圆角
 */
fun View.background(
    strokeWidth: Int = 0,
    strokeColor: Int = 0,
    solidColor: Int = Color.TRANSPARENT,
    cornerRadius: Int = 0,
    topLeftRadius: Int = 0,
    topRightRadius: Int = 0,
    bottomLeftRadius: Int = 0,
    bottomRightRadius: Int = 0
) {
    val drawable = shapeDrawable(
        strokeWidth = strokeWidth,
        strokeColor = strokeColor,
        solidColor = solidColor,
        cornerRadius = cornerRadius,
        topLeftRadius = topLeftRadius,
        topRightRadius = topRightRadius,
        bottomLeftRadius = bottomLeftRadius,
        bottomRightRadius = bottomRightRadius
    )
    background = drawable
}

/**
 * 多个状态背景 对应selector.xml
 */
fun View.stateListBackground(
    @DrawableRes normalId: Int,
    @DrawableRes pressedId: Int? = null,
    @DrawableRes selectedId: Int? = null,
    @DrawableRes checkedId: Int? = null,
    @DrawableRes enabledId: Int? = null,
    @DrawableRes disabledId: Int? = null
) {
    
    val drawable = stateListDrawable(
        getDrawable(normalId)!!,
        getDrawable(pressedId),
        getDrawable(selectedId),
        getDrawable(checkedId),
        getDrawable(enabledId),
        getDrawable(disabledId)
    )
    background = drawable
}

fun View.getDrawable(@DrawableRes id: Int?): Drawable? {
    id ?: return null
    return ContextCompat.getDrawable(context, id)
}


/**
 * 防快速点击
 *
 * @param onClicked
 */
fun View.debouncedClicks(onClicked: () -> Unit) {

}

/**
 * 显示自身及showViews数组中的view,隐藏相关的View
 *
 * @param relatedViews 隐藏的View
 */
fun View.showSelfHideOther(
    vararg relatedViews: View = emptyArray()
) {
    isVisible = true
    relatedViews.forEach {
        it.isVisible = false
    }
}

/**
 * 隐藏自身显示其他View
 *
 * @param relatedViews 显示的View
 */
fun View.hideSelfShowOther(vararg relatedViews: View = emptyArray()) {
    isVisible = false
    relatedViews.forEach {
        it.isVisible = true
    }
}
