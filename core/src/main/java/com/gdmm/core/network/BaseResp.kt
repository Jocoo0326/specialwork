package com.gdmm.core.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Php

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Java

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Java2

interface Callback {
    /**
     * 是否成功
     */
    val isSuccess: Boolean
    val error: Int
    val msg: String?
    val data: String?
}

@JsonClass(generateAdapter = true)
data class BaseResponse(
    @Json(name = "errno")
    override val error: Int = -1,
    @Json(name = "errmsg")
    override val msg: String? = null,
    @JsonString override val data: String? = null
) : Callback {
    override val isSuccess
        get() = error == 0
}

@JsonClass(generateAdapter = true)
data class BaseResp(
    @Json(name = "code")
    override val error: Int = -1,
    @Json(name = "message")
    override val msg: String? = null,
    @JsonString val content: String? = null
) : Callback {


    override val isSuccess: Boolean
        get() = error == 0

    override val data: String?
        get() = content
}

/**
 * SiteApp 接口
 *
 * @property error 200 正确
 * @property msg
 * @property data
 * @property serverTime 服务器时间
 */
@JsonClass(generateAdapter = true)
data class BaseJsonCallback(
    @Json(name = "error")
    override val error: Int = -1,
    override val msg: String? = null,
    @JsonString override val data: String? = null,
    val serverTime: Int? = null
) : Callback {

    override val isSuccess: Boolean
        get() = error == 200

}