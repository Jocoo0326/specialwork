package com.gdmm.core.extensions

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.annotation.ColorInt
import java.math.BigDecimal
import java.security.MessageDigest
import java.util.*
import java.util.regex.Pattern


fun String.asMoney(ratio: Float = 0.733f, isBold: Boolean = true): SpannableString {
    val spannableString = SpannableString("¥$this")
    val rmb = RelativeSizeSpan(ratio)
    spannableString.setSpan(rmb, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    if (isBold) {
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD), 0, length + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return spannableString
}

//手机号脱敏替换正则
const val PHONE_BLUR_REPLACE_REGEX = "$1****$2"

/**
 * 手机号脱敏
 */
fun String.blurPhone(): String {
    if (length == 11) {
        return replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), PHONE_BLUR_REPLACE_REGEX)
    }
    return this
}

/**
 * 分隔手机号 187 7899 8789
 */
fun String.divisionPhone(): String {
    if (length == 11) {
        return replace("(1\\w{2})(\\w{4})(\\w{4})".toRegex(), "$1 $2 $3")
    }
    return this
}

/**
 * 以1开头且长度11位
 *
 * @return 是否为手机号
 */
fun String.isPhone(): Boolean {
    return startsWith("1") && replace(" ", "").length == 11
}

/**
 * 是否为有效的验证码
 *
 * @return
 */
fun String.isValidCaptcha() = length == 4

/**
 * 是否为有效的密码
 *
 * @return
 */
fun String.isValidPassword() = length in 8..20

fun String?.formatDistance(): String {
    this?.let {
        val distance = it.toDouble()
        return if (distance.compare(1.0) < 0) {//小于1km
            "${(distance * 1000).toInt()}m"
        } else {
            "${it.toDouble().keepTwoDecimal()}km"
        }
    }
    return ""
}

/**
 * 高亮显示
 *
 * @param highlightText 高亮文本
 * @param highlightTextSize 高亮大小
 * @param textSize 普通文本大小
 */
fun String.highlight(
    highlightText: String,
    highlightTextSize: Int,
    textSize: Int
): SpannableStringBuilder {
    val mSpannableStringBuilder = SpannableStringBuilder(this)
    val begin = this.indexOf(highlightText)
    val end = begin + highlightText.length

    //字体大小
    mSpannableStringBuilder.setSpan(
        AbsoluteSizeSpan(highlightTextSize),
        begin,
        end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    mSpannableStringBuilder.setSpan(
        AbsoluteSizeSpan(textSize),
        end,
        length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return mSpannableStringBuilder
}

/**
 * 文本颜色高亮
 *
 * @param highlightText 高亮文本
 * @param highlightColor 高亮颜色
 * @return 高亮文本
 */
fun String.highlightColor(highlightText: String?, @ColorInt highlightColor: Int): SpannableString {
    val content = SpannableString(this)
    if (highlightText.isNullOrEmpty()) return content

    val start = content.indexOf(highlightText)
    val end = start + highlightText.length
    if (start < 0 || end > length) return content

    content.setSpan(
        ForegroundColorSpan(highlightColor),
        start,
        start + highlightText.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return content
}

fun Double.formatDecimal(scale: Int = 2): String {
//    return BigDecimal(this).setScale(scale, BigDecimal.ROUND_HALF_UP).toString()
    return keepTwoDecimal()
}

fun BigDecimal.formatDecimal(scale: Int = 2): String {
    return this.setScale(scale, BigDecimal.ROUND_HALF_UP).toString()
}

fun String?.toAmount(): String {
    if (isNullOrEmpty()) {
        return "0"
    }
    if (startsWith("¥")) {
        return substring(1)
    }
    return this
}

fun String.dealPhoneNumber(): String {
    if (this.isNotEmpty()) {
        val length = this.length
        val builder: StringBuilder = java.lang.StringBuilder()
        for (i in 0 until length) {
            builder.append(this[i])
            if (i == 2 || i == 6) {
                if (i != length - 1) {
                    builder.append(" ")
                }
            }
        }
        return builder.toString()
    }
    return ""
}

/**
 * 必须同时包含大小写字母及数字
 * 是否包含
 *
 * @param str
 * @return
 */
fun isContainAll(str: String): Boolean {
    var isDigit = false //定义一个boolean值，用来表示是否包含数字
    var isLowerCase = false //定义一个boolean值，用来表示是否包含字母
    var isUpperCase = false
    for (i in str.indices) {
        when {
            Character.isDigit(str[i]) -> {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true
            }
            Character.isLowerCase(str[i]) -> {  //用char包装类中的判断字母的方法判断每一个字符
                isLowerCase = true
            }
            Character.isUpperCase(str[i]) -> {
                isUpperCase = true
            }
        }
    }
    val regex = Regex("^[a-zA-Z0-9]+$")
    return isDigit && isLowerCase && isUpperCase && str.matches(regex)
}

/**
 * 过滤特殊字符（除了英文，数字和下划线）
 * @return
 */
fun String.filterTChar(): String {
//    val regex = Regex("^[a-z0-9A-Z_]+$")
    val regex = Regex("[^a-zA-Z0-9_]")
    return this.replace(regex, "")
}

/**
 * 过滤除了中文的其他字符（只支持中午）
 * @return
 */
fun String.filterCNChar(): String {
//    val regex = Regex("^[\\u4E00-\\u9FA5]+\$")
    val regex = Regex("[^\\u4E00-\\u9FA5]")
    return this.replace(regex, "")
}

fun String.keepTwoDecimal(scale: Int = 2): String {
    val mValue = this.toBigDecimalOrNull() ?: return "0"
    return mValue.setScale(scale, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
}

fun String.subZeroAndDot(): String? {
    var subStr: String? = null
    if (this.indexOf(".") > 0) {
        subStr = this.replace("0+?$".toRegex(), "") //去掉多余的0
        subStr = subStr.replace("[.]$".toRegex(), "") //如最后一位是.则去掉
    }
    return subStr
}

fun String.sha256(): String {
    val bytes = this.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    val sb = StringBuilder(digest.size * 2)
    val hexChars = "0123456789ABCDEF"
    digest.forEach {
        val i = it.toInt()
        sb.append(hexChars[i shr 4 and 0x0f])
        sb.append(hexChars[i and 0x0f])
    }
    return sb.toString()
}

fun String.starName(): String {
    var replcenem = ""
    //三位姓名的中间变星号
    if (this.length >= 3) {
        replcenem = this.replaceRange(1, this.length - 1, "*")
        return replcenem
    }
    if (this.length == 2) {  //两位的姓名，后面以为变星号
        replcenem = "${this.substring(0, 1)}*"
        return replcenem
    }
    return this
}
