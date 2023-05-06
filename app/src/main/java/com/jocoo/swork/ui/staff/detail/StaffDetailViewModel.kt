package com.jocoo.swork.ui.staff.detail

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import javax.inject.Inject

data class StaffDetailState(
    private val name: String? = null
) : State

class StaffDetailViewModel @Inject constructor(

) : BaseViewModel<StaffDetailState>(StaffDetailState()) {

}
