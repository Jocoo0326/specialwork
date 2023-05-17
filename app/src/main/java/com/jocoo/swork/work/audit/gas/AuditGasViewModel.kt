package com.jocoo.swork.work.audit.gas

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.bean.GasInfo
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

data class AuditGasState(
    val name: String? = null
) : State

@HiltViewModel
class AuditGasViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<AuditGasState>(AuditGasState()) {

    fun deleteGas(id: String, callback: () -> Unit) {
        launchAndCollectIn(repo.deleteGas(id)) {
            onSuccess = {
                callback()
            }
        }
    }

    fun gasModified(id: String) {
        _modifyGasFlow.tryEmit(id)
    }


    private val _modifyGasFlow = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val modifyGasFlow = _modifyGasFlow.asSharedFlow()
}

