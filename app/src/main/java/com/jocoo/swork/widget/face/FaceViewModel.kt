package com.jocoo.swork.widget.face

import androidx.lifecycle.viewModelScope
import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.gdmm.core.di.IoDispatcher
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
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
        const val CHECK_FACE = 4
        const val GAS_FIELD = "gas"
        const val SAFE_FIELD = "safeoption"
        const val FINISH_FIELD = "finish"
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
        _faceFlow.tryEmit(success_msg)
//        launchAndCollectIn(repo.searchFace("data:image/png;base64,$image")) {
//            onSuccess = {
//                _faceFlow.tryEmit(success_msg)
//            }
//            onFailed = { errorCode, errorMsg ->
//                startFace(errorMsg ?: "error")
//                true
//            }
//        }
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

    fun initCheck(ticket_id: String, field: String) {
        this.faceType = CHECK_FACE
        this.ticketId = ticket_id
        this.field = field
    }

    var ticketId: String = ""
    var field: String = ""
    fun checkFace(image: String) {
        val params = mutableMapOf<String, String>()
        params["ticket_id"] = ticketId
        params["field"] = field
//        params["show_operator"] = "1"
        params["image"] = "data:image/png;base64,$image"
        launchAndCollectIn(repo.checkProcess(params)) {
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

            CHECK_FACE -> {
                checkFace(image)
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