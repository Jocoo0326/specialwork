package com.jocoo.swork.work.audit.signature

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class AuditSignatureState(
    val name: String? = null
) : State

@HiltViewModel
class AuditSignatureViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<AuditSignatureState>(AuditSignatureState()) {

}

