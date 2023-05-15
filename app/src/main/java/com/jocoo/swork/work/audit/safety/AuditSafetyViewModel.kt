package com.jocoo.swork.work.audit.safety

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

data class AuditSafetyState(
    val showAudit: Boolean = true,
    val showNext: Boolean = true,
    val showOkCancel: Boolean = false,
) : State

@HiltViewModel
class AuditSafetyViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<AuditSafetyState>(AuditSafetyState()) {
    fun hasAuditTodo(b: Boolean) {
        setState {
            it.copy(showAudit = b)
        }
    }

    fun signMode(b: Boolean) {
        setState {
            it.copy(showOkCancel = b)
        }
    }

    fun uploadImage(toByteArray: ByteArray) {
        launchAndCollectIn(repo.uploadImage(toByteArray)) {

        }
    }
    private val _uploadImageFlow = MutableSharedFlow<Boolean>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uploadImageFlow = _uploadImageFlow.asSharedFlow()

}

