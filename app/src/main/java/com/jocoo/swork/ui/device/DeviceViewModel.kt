package com.jocoo.swork.ui.device

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State

data class DeviceState(
    val name: String = ""
) : State

class DeviceViewModel : BaseViewModel<DeviceState>(DeviceState()) {

}
