package com.jocoo.swork.work.audit

import android.content.Context
import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.gdmm.core.network.SessionManager
import com.jocoo.swork.bean.FaceConfigInfo
import com.jocoo.swork.bean.GasInfo
import com.jocoo.swork.bean.GasTableOptionsInfo
import com.jocoo.swork.bean.TicketDetailInfo
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

data class WorkAuditState(
    val detail: TicketDetailInfo? = null,
    val gasConfig: GasTableOptionsInfo? = null,
    val gasList: List<GasInfo>? = null,
    val isStop: Boolean = false,
    val newGasItem: GasInfo = GasInfo(),
    val faceConfigs: List<FaceConfigInfo>? = null
) : State {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}

@HiltViewModel
class WorkAuditViewModel @Inject constructor(
    private val repo: ApiRepo,
    @ApplicationContext private val appContext: Context
) : BaseViewModel<WorkAuditState>(WorkAuditState()) {

    var workType: Int = 0
    var workId: String = ""

    fun getTicketInfo() {
        launchAndCollectIn(repo.getTicketInfo(workId)) {
            onSuccess = {
                setState { state ->
                    state.copy(detail = it.info, isStop = it.info.is_stop == "1")
                }
            }
        }
    }

    fun nextPage() {
        _nextPageFlow.tryEmit(true)
    }

    private val _nextPageFlow = MutableSharedFlow<Boolean>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val nextPageFlow = _nextPageFlow.asSharedFlow()

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
        params["type_id"] = if (state.value.isStop) "2" else "1"
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

    fun gasList(type: String) {
        launchAndCollectIn(repo.getGasList(workId, type)) {
            onSuccess = {
                setState { state ->
                    state.copy(gasList = it.list)
                }
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

    fun setStop(b: Boolean) {
        launchAndCollectIn(
            if (b) {
                repo.setStop(workId)
            } else {
                repo.setContinue(workId)
            }
        ) {
            onSuccess = {
                _setStopFlow.tryEmit(b)
                setState { state ->
                    state.copy(isStop = b)
                }
                gasList(if (b) "2" else "1")
            }
        }
    }

    private val _setStopFlow = MutableSharedFlow<Boolean>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val setStopFlow = _setStopFlow.asSharedFlow()

    fun getFaceConfigs() {
        val sm = SessionManager.getInstance(appContext)
        val orgId = sm.userInfo?.org_id ?: ""
        launchAndCollectIn(repo.getFaceConfigs(orgId)) {
            onSuccess = {
                setState { state ->
                    state.copy(faceConfigs = it.lists)
                }
            }
        }
    }
}
