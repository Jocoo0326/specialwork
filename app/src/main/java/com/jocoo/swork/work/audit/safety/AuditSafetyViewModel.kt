package com.jocoo.swork.work.audit.safety

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class AuditSafetyState(
    val name: String? = null
) : State

@HiltViewModel
class AuditSafetyViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<AuditSafetyState>(AuditSafetyState()) {

}

