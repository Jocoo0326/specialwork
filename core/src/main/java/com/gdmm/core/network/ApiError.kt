package com.gdmm.core.network


class ApiError(val code: Int, message: String? = null) : RuntimeException(message) {
    
    val isSuccess
        get() = code == 0
}