package com.jocoo.swork.work.preview.baseinfo

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class PreviewBaseInfoState(
    val name: String? = null
) : State

@HiltViewModel
class PreviewBaseInfoViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<PreviewBaseInfoState>(PreviewBaseInfoState()) {

}
