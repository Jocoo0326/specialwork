package com.gdmm.core.error

import android.content.Context
import com.gdmm.core.BaseApplication
import com.gdmm.core.Event
import com.gdmm.core.extensions.showShortToast


/**
 * 接口响应错误处理
 */
fun Context.onResponseOperator(
    event: Event,
    onLoading: (() -> Unit)? = null,
    onCompleted: (() -> Unit)? = null,
    onSuccess: ((String) -> Unit)? = null,
    onError: ((Throwable) -> Boolean)? = null,
    onFailed: ((Int, String?) -> Boolean)? = null
) {
    when (event) {
        is Event.Failed -> {
            if (onFailed?.invoke(event.code, event.message) == true) return
            handleFailed(event)
        }

        is Event.Exception -> {
            if (onError?.invoke(event.ex) == true) return
//            showShortToast(R.string.oil_core_server_error)
        }

        is Event.Loading -> {
            onLoading?.invoke()
        }

        is Event.Success -> {
            onSuccess?.invoke(event.tag)
        }

        is Event.Completed -> {
            onCompleted?.invoke()
        }
    }
}

private fun handleFailed(event: Event.Failed) {
    val applicationContext = BaseApplication.applicationContext()

    event.message?.let {
        applicationContext.showShortToast(it)
    }
}

//@IntDef(SUCCESS, RE_LOGIN)
//@Retention(AnnotationRetention.SOURCE)
//annotation class ResultCode {
//    companion object {
//        const val SUCCESS = 0
//        const val NEED_LOGIN = 4 //账号未登陆
//        const val RE_LOGIN = 40 //账号同时登陆了多台设备
//        const val VERIFY_LICENSE_PLATE_NUMBER_FAILED = 13 //车牌验证失败
//    }
//}