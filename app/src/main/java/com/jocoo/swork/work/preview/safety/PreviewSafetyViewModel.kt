package com.jocoo.swork.work.preview.safety

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class PreviewSafetyState(
    private val name: String? = null
) : State

@HiltViewModel
class PreviewSafetyViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<PreviewSafetyState>(PreviewSafetyState()) {

}
