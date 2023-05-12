package com.jocoo.swork.work.preview

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class WorkPreviewState(
    private val name: String? = null
) : State

@HiltViewModel
class WorkPreviewViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<WorkPreviewState>(WorkPreviewState()) {

}
