package com.jocoo.swork.ui.home

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State

data class HomeState(
    val name: String = ""
) : State

class HomeViewModel : BaseViewModel<HomeState>(HomeState()) {

}