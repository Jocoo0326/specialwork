package com.gdmm.core.network

import com.gdmm.core.network.ApiResponse.*

sealed class ApiResponse<T> {
    
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Failed<T>(val code: Int = -1, val message: String?) : ApiResponse<T>()
    data class Error<T>(val exception: Throwable) : ApiResponse<T>()
    object Loading : ApiResponse<Nothing>()
    object Completed : ApiResponse<Nothing>()
    
    companion object {
        fun <T> success(data: T) = Success(data)
        fun <T> failed(code: Int = -1, message: String?) = Failed<T>(code, message)
        fun <T> error(exception: Throwable) = Error<T>(exception)
        fun <T> completed() = Completed as T
        fun <T> loading() = Loading as T
    }
    
    inline fun onSuccess(block: (T) -> Unit): ApiResponse<T> = apply {
        if (this is Success) {
            block(data)
        }
    }
    
    inline fun onFailure(block: (Int, String?) -> Unit): ApiResponse<T> = apply {
        if (this is Failed) {
            block(code, message)
        }
    }
    
    inline fun onError(block: (Throwable) -> Unit): ApiResponse<T> = apply {
        if (this is Error) {
            block(exception)
        }
    }
    
    inline fun onLoading(block: () -> Unit): ApiResponse<T> = apply {
        if (this is Loading) {
            block()
        }
    }
    
    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Failed -> "Failed[code=$code,message=$message]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
            Completed -> "load completed"
        }
    }
}

inline fun <T> ApiResponse<T>.parseData(
    onSuccess: (data: T) -> Unit = {},
    onFailed: (errorCode: Int, errorMsg: String?) -> Unit = { _, _ -> },
    onError: (e: Throwable) -> Unit = {},
    onLoad: () -> Unit = {},
    onComplete: () -> Unit = {}
) {
    when (this) {
        is Success -> {
            onSuccess(data)
            onComplete()
        }
        is Failed -> {
            onFailed(code, message)
            onComplete()
        }
        is Error -> {
            onError(exception)
            onComplete()
        }
        is Loading -> onLoad()
        else -> {
        
        }
    }
}

class ResultBuilder<T> {
    var onSuccess: (data: T) -> Unit = {}
    var onFailed: (errorCode: Int, errorMsg: String?) -> Boolean = { _, _ -> false }
    var onError: (e: Throwable) -> Boolean = { false }
    var onLoad: () -> Boolean = { false }
    var onComplete: () -> Boolean = { false }
}


fun <T> ApiResponse<T>.successOr(fallback: T): T {
    return (this as? Success<T>)?.data ?: fallback
}

