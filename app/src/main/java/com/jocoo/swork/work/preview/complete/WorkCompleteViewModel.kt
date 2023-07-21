package com.jocoo.swork.work.preview.complete

import android.content.Context
import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.gdmm.core.network.SessionManager
import com.jocoo.swork.bean.FaceConfigInfo
import com.jocoo.swork.data.ApiRepo
import com.jocoo.swork.widget.face.FaceResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

data class WorkCompleteState(
    val faceConfigs: List<FaceConfigInfo>? = null
) : State

@HiltViewModel
class WorkCompleteViewModel @Inject constructor(
    private val repo: ApiRepo,
    @ApplicationContext private val appContext: Context
) : BaseViewModel<WorkCompleteState>(WorkCompleteState()) {

    fun setAccept(id: String, faceResult: FaceResult?) {
        val params = mutableMapOf<String, String>()
        params["ticket_id"] = id
        params["content"] = comment
        params["sign"] = sign
        faceResult?.let {
            params["operator_id"] = "${faceResult.operatorId}"
            params["face_path"] = "${faceResult.facePath}"
        }
        launchAndCollectIn(repo.setAccept(params)) {
            onSuccess = {
                _setAcceptFlow.tryEmit(true)
            }
        }
    }

    var comment: String = ""
    var sign: String = ""

    private val _setAcceptFlow = MutableSharedFlow<Boolean>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val setAcceptFlow = _setAcceptFlow.asSharedFlow()

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
