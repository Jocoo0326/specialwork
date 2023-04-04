package com.gdmm.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdmm.core.extensions.launchAndCollectIn
import com.gdmm.core.network.ApiResponse
import com.gdmm.core.network.ResultBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Event {
    object Loading : Event()
    object Completed : Event()
    data class Exception(val ex: Throwable) : Event()
    data class Failed(val code: Int, val message: String?) : Event()

    /**
     * 请求成功
     *
     * @property tag 区分哪个接口
     */
    data class Success(val tag: String) : Event()
}

interface State


abstract class BaseViewModel<STATE : State>(initialState: STATE) : ViewModel() {
    
    private val _state = MutableStateFlow(initialState)
    
    /**
     * 暴露给UI层的状态
     */
    val state = _state.asStateFlow()
    
    /**
     * 当前UI状态
     */
    val currentState: STATE get() = state.value
    
    /**
     * 更新ViewModel状态
     *
     * @param update Lambda 回调(参数旧状态,返回值新状态)
     * @return 更新后的状态
     */
    protected fun setState(update: (old: STATE) -> STATE): STATE = _state.updateAndGet(update)

    /**
     * 单次事件，针对showToast,showDialog,后续不需要持续观察
     * {@see "https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150"}
     * {@see 'https://proandroiddev.com/android-singleliveevent-redux-with-kotlin-flow-b755c70bb055'}
     */
    private val eventChannel = MutableSharedFlow<Event>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val eventFlows: SharedFlow<Event> = eventChannel


    /**
     * 发起请求
     *
     * @param T
     * @param flow
     * @param tag 唯一一个请求的唯一标识
     * @param listenerBuilder
     */
    fun <T> launchAndCollectIn(
        flow: Flow<ApiResponse<T>>,
        tag: String? = null,
        listenerBuilder: ResultBuilder<T>.() -> Unit
    ) {
        val listener = ResultBuilder<T>().also(listenerBuilder)

        flow.launchAndCollectIn(
            viewModelScope,
            onLoad = {
                if (!listener.onLoad()) {//listener.onLoad()返回true则由调用方处理loading
                    eventChannel.tryEmit(Event.Loading)
                }
            },
            onFailed = { code, message ->
                if (!listener.onFailed(code, message)) {//listener.onError(exception)返回true则由调用方处理异常
                    eventChannel.tryEmit(Event.Failed(code, message))
                }
            },
            onError = { exception ->
                if (!listener.onError(exception)) {//listener.onError(exception)返回true则由调用方处理异常
                    eventChannel.tryEmit(Event.Exception(exception))
                }
            },
            onComplete = {
                if (!listener.onComplete()) {
                    eventChannel.tryEmit(Event.Completed)
                }
            },
        ) { data ->
            listener.onSuccess(data)
            tag?.let {
                eventChannel.tryEmit(Event.Success(it))
            }
        }
    }
}
