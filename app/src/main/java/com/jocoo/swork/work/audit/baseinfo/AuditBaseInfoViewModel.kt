package com.jocoo.swork.work.audit.baseinfo

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class AuditBaseInfoState(
    val name: String? = null
) : State

@HiltViewModel
class AuditBaseInfoViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<AuditBaseInfoState>(AuditBaseInfoState()) {

}

