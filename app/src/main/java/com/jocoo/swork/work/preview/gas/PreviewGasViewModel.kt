package com.jocoo.swork.work.preview.gas

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class PreviewGasState(
    private val name: String? = null
) : State

@HiltViewModel
class PreviewGasViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<PreviewGasState>(PreviewGasState()) {

    fun deleteGas(id: String, callback: () -> Unit) {
        launchAndCollectIn(repo.deleteGas(id)) {
            onSuccess = {
                callback()
            }
        }
    }
}