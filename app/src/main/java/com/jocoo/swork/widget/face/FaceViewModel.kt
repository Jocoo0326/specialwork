package com.jocoo.swork.widget.face

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.gdmm.core.di.IoDispatcher
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

data class FaceState(
    private val name: String? = null
) : State

@HiltViewModel
class FaceViewModel @Inject constructor(
    private val repo: ApiRepo,
    @IoDispatcher val io: CoroutineContext
) : BaseViewModel<FaceState>(FaceState()) {

    companion object {
        const val start_msg = "start"
        const val success_msg = "success"
        const val CREATE_FACE = 1
        const val SEARCH_FACE = 2
        const val MATCH_FACE = 3
    }

    var faceType: Int = CREATE_FACE
    var faceUserId: String = ""
    fun createFace(image: String) {
        launchAndCollectIn(repo.createFace(faceUserId, "data:image/png;base64,$image")) {
            onSuccess = {
                _faceFlow.tryEmit(success_msg)
            }
            onFailed = { errorCode, errorMsg ->
                startFace(errorMsg ?: "error")
                true
            }
        }
    }

    fun searchFace(image: String) {
        launchAndCollectIn(repo.searchFace("data:image/png;base64,$image")) {
            onSuccess = {
                _faceFlow.tryEmit(success_msg)
            }
            onFailed = { errorCode, errorMsg ->
                startFace(errorMsg ?: "error")
                true
            }
        }
    }

    fun matchFace(image: String) {
        launchAndCollectIn(repo.matchFace(faceUserId, "data:image/png;base64,$image")) {
            onSuccess = {
                _faceFlow.tryEmit(success_msg)
            }
            onFailed = { errorCode, errorMsg ->
                startFace(errorMsg ?: "error")
                true
            }
        }
    }

    fun requestFace(image: String) {
        when (faceType) {
            CREATE_FACE -> {
                createFace(image)
            }

            SEARCH_FACE -> {
                searchFace(image)
            }

            else -> {
                matchFace(image)
            }
        }
    }

    fun startFace(msg: String = start_msg) {
        flow {
            delay(500)
            emit(1)
        }.flowOn(io).onEach {
            _faceFlow.tryEmit(msg)
        }.launchIn(viewModelScope)
    }

    private val _faceFlow = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val faceFlow = _faceFlow.asSharedFlow()
}