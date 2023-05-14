package com.jocoo.swork.work.audit

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.bean.TicketDetailInfo
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

data class WorkAuditState(
    val detail: TicketDetailInfo? = null,
) : State

@HiltViewModel
class WorkAuditViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<WorkAuditState>(WorkAuditState()) {

    var workType: Int = 0
    var workId: String = ""

    fun getTicketInfo() {
        launchAndCollectIn(repo.getTicketInfo(workId)) {
            onSuccess = {
                setState { state ->
                    state.copy(detail = it.info)
                }
            }
        }
    }

    fun nextPage() {
        _nextPageFlow.tryEmit(true)
    }

    private val _nextPageFlow = MutableSharedFlow<Boolean>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val nextPageFlow = _nextPageFlow.asSharedFlow()
}
