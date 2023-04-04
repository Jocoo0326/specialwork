package com.gdmm.core.extensions

import androidx.lifecycle.*
import com.gdmm.core.network.ApiError
import com.gdmm.core.network.ApiResponse
import com.gdmm.core.network.parseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

inline fun <T> Flow<T>.observeWithLifecycle(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend (T) -> Unit
): Job {
    return flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState)
        .onEach { state -> action(state) }
        .launchIn(lifecycleOwner.lifecycleScope)
}


inline fun <T> Flow<T>.observeWithLifecycle(
    lifecycle: Lifecycle,
    lifecycleScope: LifecycleCoroutineScope,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend (T) -> Unit
): Job {
    return flowWithLifecycle(lifecycle, minActiveState)
        .onEach { state -> action(state) }
        .launchIn(lifecycleScope)
}

/**
 *
 * @param T
 * @param coroutineDispatcher
 * @return
 */
fun <T> Flow<ApiResponse<T>>.safeApiCall(
    coroutineDispatcher: CoroutineContext,
): Flow<ApiResponse<T>> {
    return catch { ex ->
        ex.printStackTrace()
        when (ex) {
            is ApiError ->
                emit(ApiResponse.failed(ex.code, ex.message))
            else -> emit(ApiResponse.error(ex))
        }
    }.onStart {
        emit(ApiResponse.loading())
    }/*.onCompletion {
        emit(ApiResponse.completed())
    }*/.flowOn(coroutineDispatcher)
}

fun <T, M> StateFlow<T>.map(
    coroutineScope: CoroutineScope, mapper: (value: T) -> M
): StateFlow<M> = map { mapper(it) }.stateIn(
    coroutineScope, SharingStarted.Eagerly, mapper(value)
)

inline fun <T> Flow<ApiResponse<T>>.launchAndCollectIn(
    viewModelScope: CoroutineScope,
    crossinline onLoad: () -> Unit = {},
    crossinline onFailed: (errorCode: Int, errorMsg: String?) -> Unit = { _, _ -> },
    crossinline onError: (e: Throwable) -> Unit = {},
    crossinline onComplete: () -> Unit = {},
    crossinline onSuccess: (data: T) -> Unit = {},
) {
    onEach { apiResponse ->
        apiResponse.parseData(onSuccess, onFailed, onError, onLoad, onComplete)
    }.launchIn(viewModelScope)
}

