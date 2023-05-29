package com.jocoo.swork.work.audit.signature

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.bean.ProcessOpinion
import com.jocoo.swork.bean.SignOption
import com.jocoo.swork.bean.TicketOptionsResp
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

data class AuditSignatureState(
    val optionsList: TicketOptionsResp? = null,
) : State

@HiltViewModel
class AuditSignatureViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<AuditSignatureState>(AuditSignatureState()) {

    fun getTicketOpinions(id: String) {
        launchAndCollectIn(repo.getTicketOpinions(id)) {
            onSuccess = {
                setState { state ->
                    state.copy(optionsList = it)
                }
            }
        }
    }

    fun setOpinions(id: String, list: List<Any?>?) {
        if (list.isNullOrEmpty()) return
        val params = mutableMapOf<String, String>()
        params["ticket_id"] = id
        list.filterIsInstance<SignOption>().forEach {
            params["signList[${it.key}]"] = "${it.sign}"
        }
        list.filterIsInstance<ProcessOpinion>().forEachIndexed { index, it ->
            params["opinions[${index}][id]"] = "${it.id}"
            params["opinions[${index}][sign]"] = "${it.sign}"
            params["opinions[${index}][content]"] = "${it.comment}"
        }
        println(params)
//        launchAndCollectIn(repo.setOpinions(params)) {
//            onSuccess = {
//                _setOpinionFlow.tryEmit(true)
//            }
//        }
    }

    private val _setOpinionFlow = MutableSharedFlow<Boolean>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val setOpinionFlow = _setOpinionFlow.asSharedFlow()
}

