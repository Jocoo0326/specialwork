package com.jocoo.swork.ui.staff.list

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import javax.inject.Inject

data class StaffListState(
    private val name: String? = null
) : State

class StaffListViewModel @Inject constructor(

) : BaseViewModel<StaffListState>(StaffListState()) {

}