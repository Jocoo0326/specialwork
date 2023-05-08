package com.jocoo.swork.ui.splash

import androidx.lifecycle.viewModelScope
import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.gdmm.core.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

data class SplashState(
    val countDownSeconds: Int = SPLASH_COUNT_DOWN_SECS
) : State

const val SPLASH_COUNT_DOWN_SECS = 2

@HiltViewModel
class SplashViewModel @Inject constructor(
    @IoDispatcher val io: CoroutineContext
) : BaseViewModel<SplashState>(SplashState()) {

    fun countDown(initCountDown: Int = SPLASH_COUNT_DOWN_SECS) {
        if (initCountDown <= 0) return
        flow {
            var cd = initCountDown
            while (true) {
                emit(cd)
                cd--
                if (cd < 0) {
                    break
                }
                delay(1000)
            }
        }.flowOn(io).onEach {
            setState { state ->
                state.copy(countDownSeconds = it)
            }
        }.launchIn(viewModelScope)
    }
}