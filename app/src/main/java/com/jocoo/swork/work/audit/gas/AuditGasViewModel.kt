package com.jocoo.swork.work.audit.gas

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
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

}

