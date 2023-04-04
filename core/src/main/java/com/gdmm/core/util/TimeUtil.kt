package com.gdmm.core.util


import androidx.annotation.StringDef
import androidx.core.text.isDigitsOnly
import com.gdmm.core.util.DateFormatPattern.Companion.DEFAULT_FORMAT
import com.gdmm.core.util.DateFormatPattern.Companion.FORMAT_YMD_HM
import com.gdmm.core.util.DateFormatPattern.Companion.FORMAT_YMD_HMS
import com.gdmm.core.util.DateFormatPattern.Companion.FORMAT_YMD_HM_2
import com.gdmm.core.util.DateFormatPattern.Companion.GMT
import com.gdmm.core.util.DateFormatPattern.Companion.YYYY_MM_DD
import com.gdmm.core.util.DateFormatPattern.Companion.YYYY_MM_DD_HH_MM_SS
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@StringDef(
    value = [
        DEFAULT_FORMAT,
        FORMAT_YMD_HMS,
        FORMAT_YMD_HM,
        FORMAT_YMD_HM_2,
        GMT,
        YYYY_MM_DD_HH_MM_SS,
        YYYY_MM_DD
    ]
)
@Retention(AnnotationRetention.SOURCE)
annotation class DateFormatPattern {

    companion object {
        const val DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss"
        const val FORMAT_YMD_HMS = "yyyy.MM.dd HH:mm:ss"
        const val FORMAT_YMD_HM = "yyyy.MM.dd HH:mm"
        const val FORMAT_YMD_HM_2 = "yyyy-MM-dd HH:mm"
        const val GMT = "EEE, d MMM yyyy HH:mm:ss z"
        const val YYYY_MM_DD_HH_MM_SS = "yyyy.MM.dd HH:mm:ss"
        const val YYYY_MM_DD = "yyyy-MM-dd"
    }
}


fun String.toOtherDateStr(originFormat: String, targetFormat: String): String {
    return try {
        val millisSecond = SimpleDateFormat(originFormat).parse(this)?.time ?: -1
        SimpleDateFormat(targetFormat).format(millisSecond)
    } catch (e: Exception) {
        this
    }
}

/**
 * 从一种格式日期字符 转 另一种格式日期字符串
 *
 * @param sourcePattern 源日期字符串格式
 * @param targetPattern 目标日期字符串格式
 * @param locale Local 默认 Locale.getDefault()
 * @param dateFormat dateFormat
 *
 * @return 日期字符串
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
inline fun String?.stringConvertToDateStr(
    sourcePattern: String,
    targetPattern: String,
    locale: Locale = Locale.getDefault(),
    dateFormat: (pattern: String, locale: Locale) -> DateFormat = ::dateFormat
): String? =
    this?.let {
        return@let try {
            val date = dateFormat(sourcePattern, locale).parse(it)
            dateFormat(targetPattern, locale).format(date)
        } catch (e: Exception) {
            return null
        }
    }

/**
 * 秒转时间
 *
 * @param pattern 日期格式yyyy-MM-dd
 * @param locale Local 默认 Locale.getDefault()
 * @param dateFormat dateFormat
 * @return 日期字符串
 */
inline fun Long.convertToDateStr(
    pattern: String = YYYY_MM_DD,
    locale: Locale = Locale.getDefault(),
    dateFormat: (pattern: String, locale: Locale) -> DateFormat = ::dateFormat
): String? = try {
    dateFormat(pattern, locale).format(Date(this * 1000L))
} catch (e: Exception) {
    null
}

inline fun Long.convertToDateStrByMillisecond(
    pattern: String = YYYY_MM_DD,
    locale: Locale = Locale.getDefault(),
    dateFormat: (pattern: String, locale: Locale) -> DateFormat = ::dateFormat
): String? = try {
    dateFormat(pattern, locale).format(Date(this))
} catch (e: Exception) {
    null
}

/**
 * 字符串转日期
 *
 * @param pattern 日期格式yyyy-MM-dd
 * @param locale Local 默认 Locale.getDefault()
 * @param dateFormat dateFormat
 * @return 日期
 */
inline fun String?.stringConvertToDate(
    pattern: String = YYYY_MM_DD,
    locale: Locale = Locale.getDefault(),
    dateFormat: (pattern: String, locale: Locale)
    -> DateFormat = ::dateFormat
): Date? =
    this?.let {
        dateFormat(pattern, locale).parse(it)
    }


/**
 * 字符串转毫秒
 *
 * @param pattern 日期格式
 * @param locale
 * @param dateFormat dateFormat
 * @return 毫秒
 */
inline fun String.toDateLong(
    pattern: String = DEFAULT_FORMAT,
    locale: Locale = Locale.getDefault(),
    dateFormat: (pattern: String, locale: Locale)
    -> DateFormat = ::dateFormat
): Long = try {
    dateFormat(pattern, locale).parse(this)?.time ?: 0
} catch (e: Exception) {
    0
}


fun dateFormat(pattern: String, locale: Locale): SimpleDateFormat {
    val dateFormat = SimpleDateFormat(pattern, locale)
    dateFormat.timeZone = TimeZone.getTimeZone("GMT+8")
    return dateFormat
}

fun String?.formatDate(
    pattern: String = DEFAULT_FORMAT,
    sourcePattern: String = YYYY_MM_DD
): String {
    if (this.isNullOrEmpty()) return ""
    val date = try {
        if (this.isDigitsOnly()) {
            val timeStamp = this.toLong()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeStamp
            calendar.time
        } else {
            this.stringConvertToDate(sourcePattern)
        }
    } catch (e: Exception) {
        null
    } ?: return this

    return dateFormat(pattern, Locale.getDefault()).format(date)
}