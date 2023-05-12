package com.jocoo.swork.work.audit

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class WorkAuditState(
    private val name: String? = null
) : State

@HiltViewModel
class WorkAuditViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<WorkAuditState>(WorkAuditState()) {

}
