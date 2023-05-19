package com.jocoo.swork.work.preview.complete

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

data class WorkCompleteState(
    private val name: String? = null
) : State

@HiltViewModel
class WorkCompleteViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<WorkCompleteState>(WorkCompleteState()) {

    fun setAccept(id: String) {
        val params = mutableMapOf<String, String>()
        params["ticket_id"] = id
        params["content"] = comment
        params["sign"] = sign
        launchAndCollectIn(repo.setAccept(params)) {
            onSuccess = {
                _setAcceptFlow.tryEmit(true)
            }
        }
    }

    var comment: String = ""
    var sign: String = ""

    private val _setAcceptFlow = MutableSharedFlow<Boolean>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val setAcceptFlow = _setAcceptFlow.asSharedFlow()
}
