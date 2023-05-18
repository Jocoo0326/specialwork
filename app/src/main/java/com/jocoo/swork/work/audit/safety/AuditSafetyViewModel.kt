package com.jocoo.swork.work.audit.safety

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.bean.CheckInfo
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

    fun checkSafety(
        ticketId: String?,
        signImage: String?,
        checkList: List<CheckInfo>?,
        addCheckList: List<CheckInfo>?
    ) {
        val params = mutableMapOf<String, String>()
        params["ticket_id"] = "$ticketId"
        params["sign"] = if (signImage.isNullOrEmpty()) "\"\"" else signImage
        checkList?.forEachIndexed { index, it ->
            params["check_res[$index][id]"] = "${it.id}"
            params["check_res[$index][res]"] = "1"
        }
        addCheckList?.forEachIndexed { index, it ->
            params["add_check_res[$index][id]"] = "${it.id}"
            params["add_check_res[$index][res]"] = "1"
        }
        launchAndCollectIn(repo.checkSafety(params)) {
            onSuccess = {
                _checkSafetyFlow.tryEmit(true)
            }
        }
    }

    private val _checkSafetyFlow = MutableSharedFlow<Boolean>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val checkSafetyFlow = _checkSafetyFlow.asSharedFlow()
}

