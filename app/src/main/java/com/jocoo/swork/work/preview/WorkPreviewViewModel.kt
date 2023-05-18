package com.jocoo.swork.work.preview

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.bean.GasInfo
import com.jocoo.swork.bean.GasTableOptionsInfo
import com.jocoo.swork.bean.TicketDetailInfo
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

data class WorkPreviewState(
    val detail: TicketDetailInfo? = null,
    val gasConfig: GasTableOptionsInfo? = null,
    val newGasItem: GasInfo = GasInfo()
) : State

@HiltViewModel
class WorkPreviewViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<WorkPreviewState>(WorkPreviewState()) {

    var workType: Int = 0
    var workId: String = ""

    fun getTicketInfo() {
        launchAndCollectIn(repo.getTicketInfo(workId)) {
            onSuccess = {
                setState { state ->
                    state.copy(detail = it.info)
                }
            }
        }
    }

    fun fetchGasConfig(callback: () -> Unit) {
        launchAndCollectIn(repo.getGasTableOptions(workId)) {
            onSuccess = {
                setState { state ->
                    state.copy(gasConfig = it)
                }
                callback()
            }
        }
    }

    fun addGasItem() {
        val params = mutableMapOf<String, String>()
        params["ticket_id"] = workId
        state.value.newGasItem.let {
            params["gas_type_id"] = "${it.gas_type_id}"
            params["gas_type_name"] = "${it.gas_type_name}"
            params["concentration"] = "${it.concentration}"
            params["unit_type"] = "${it.unit_type}"
            params["unit_name"] = "${it.unit_name}"
            params["standard"] = "${it.standard}"
            params["group_id"] = "${it.group_id}"
            params["group_name"] = "${it.group_name}"
            params["place"] = "${it.place}"
            params["analysis_time"] = "${it.analysis_time}"
            params["analysis_user"] = "${it.analysis_user}"
        }
        launchAndCollectIn(repo.addGas(params)) {
            onSuccess = {
                getTicketInfo()
            }
        }
    }

    fun gasModified(id: String, gas: GasInfo) {
        val params = mutableMapOf<String, String>()
        params["gas_data_id"] = id
        params["ticket_id"] = workId
        gas.let {
            params["gas_type_id"] = "${it.gas_type_id}"
            params["gas_type_name"] = "${it.gas_type_name}"
            params["concentration"] = "${it.concentration}"
            params["unit_type"] = "${it.unit_type}"
            params["unit_name"] = "${it.unit_name}"
            params["standard"] = "${it.standard}"
            params["group_id"] = "${it.group_id}"
            params["group_name"] = "${it.group_name}"
            params["place"] = "${it.place}"
            params["analysis_time"] = "${it.analysis_time}"
            params["analysis_user"] = "${it.analysis_user}"
        }
        launchAndCollectIn(repo.editGas(params)) {
            onSuccess = {
                _modifyGasFlow.tryEmit(id)
            }
        }
    }


    private val _modifyGasFlow = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val modifyGasFlow = _modifyGasFlow.asSharedFlow()

    fun resetNewGasItem() {
        setState { state ->
            state.copy(newGasItem = GasInfo())
        }
    }
}
