package com.gdmm.core.util

import android.os.CountDownTimer
import android.os.Looper
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SimpleCountDownTimer private constructor(millisInFuture: Long, countDownInterval: Long) {

    private val tick: Flow<Long> = callbackFlow {
        if (Looper.myLooper() == null) {
            throw IllegalStateException("Can't create SimpleCountDownTimer inside thread that has not called Looper.prepare() Just use Dispatchers.Main")
        }
        val timer = object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                trySend(millisUntilFinished)
            }

            override fun onFinish() {
                close()
            }
        }.start()
        awaitClose { timer.cancel() }
    }

    companion object {
        @JvmStatic
        fun create(millisInFuture: Long = 60_000, countDownInterval: Long = 1000) =
            SimpleCountDownTimer(millisInFuture + 200, countDownInterval).tick
    }
}