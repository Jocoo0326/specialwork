package com.jocoo.swork.work.preview.signature

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class PreviewSignatureState(
    private val name: String? = null
) : State

@HiltViewModel
class PreviewSignatureViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<PreviewSignatureState>(PreviewSignatureState()) {

}
