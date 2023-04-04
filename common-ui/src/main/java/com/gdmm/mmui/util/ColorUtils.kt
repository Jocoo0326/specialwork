package com.gdmm.mmui.util

import android.animation.ArgbEvaluator


/**
 * 计算颜色值
 *
 * @param fraction [0,1]
 * @param startColor 开始颜色
 * @param endColor 结束颜色
 * @return 计算后的值
 */
fun calcColor(fraction: Float, startColor: Int, endColor: Int): Int {

    return ArgbEvaluator().evaluate(fraction, startColor, endColor) as Int
}