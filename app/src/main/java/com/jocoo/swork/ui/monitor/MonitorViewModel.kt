package com.jocoo.swork.ui.monitor

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State


data class MonitorState(
    val name: String = ""
) : State

class MonitorViewModel : BaseViewModel<MonitorState>(MonitorState()) {

}
