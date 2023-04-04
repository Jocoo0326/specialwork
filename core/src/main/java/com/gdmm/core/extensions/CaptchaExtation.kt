package com.gdmm.core.extensions

import android.content.Context
import com.gdmm.core.util.fromJson
import com.njgdmm.lib.dialog.DialogUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException

/**
 * @author Triple
 * @date 2023/3/8
 */
/**
 * Android 腾讯验证码sdk 回调
 *
 * @property randStr 随机数
 * @property ticket 票据
 */
@JsonClass(generateAdapter = true)
data class TencentCaptchaInfo(
    @Json(name = "randstr")
    val randStr: String?,

    val ticket: String?
)

/**
 * 发送短信验证码之前-先调用腾讯图形验证码
 *
 * @param context 上下文
 * @param needToIntercept 是否需要使用腾讯图形验证码拦截，然后再发短信
 * @param callback 获得腾讯图形验证码回调
 */
fun onCaptchaIntercept(
    context: Context,
    needToIntercept: Boolean = true,
    callback: (randStr: String, ticket: String) -> Unit
) {
    if (!needToIntercept) {//不需要拦截
        callback("", "")
        return
    }

    DialogUtil.showTencentCaptchaDialog(
        context,
    ) { result: String? ->
        result ?: return@showTencentCaptchaDialog

        fromJson(result,TencentCaptchaInfo::class.java)?.apply {
            randStr ?: return@showTencentCaptchaDialog
            ticket ?: return@showTencentCaptchaDialog
            callback(randStr, ticket)
        }
    }
}

