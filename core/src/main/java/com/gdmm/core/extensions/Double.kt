package com.gdmm.core.extensions

import java.math.RoundingMode

/**
 * 两个double 比较大小
 *
 * @param other 另一个数
 * @return 1 大于,0等于,-1小于
 */
fun Double.compare(other: Double) = toBigDecimal().compareTo(other.toBigDecimal())

/**
 * 两个double比较大小
 *
 * @param other 另一个double
 * @return true 大于,false 小于或等于
 */
fun Double.moreThan(other: Double) = (toBigDecimal().compareTo(other.toBigDecimal())) > 0

/**
 * 两个double比较大小
 *
 * @param other 另一个double
 * @return true 小于,false 大于或等于
 */
fun Double.lessThan(other: Double) = (toBigDecimal().compareTo(other.toBigDecimal())) < 0




/**
 * 去掉小数位的0
 */
fun Double.keepTwoDecimal(): String {
    return trimTrailingZero(toBigDecimal().setScale(2, RoundingMode.HALF_UP).toString())
}

fun trimTrailingZero(s: String): String {
    // 不存在小数点，或者含有多个小数点，则不处理
    if (!s.contains(".") || s.indexOf(".") != s.lastIndexOf(".")) return s
    //先去掉末尾的0，再去掉末尾的0
    return if (s.contains(".")) s.replace("0*$".toRegex(), "").replace("\\.$".toRegex(), "") else s
}