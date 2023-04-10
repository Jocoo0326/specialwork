package com.jocoo.swork.ui.staff

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State


data class StaffState(
    val name: String = ""
) : State

class StaffViewModel : BaseViewModel<StaffState>(StaffState()) {

}
