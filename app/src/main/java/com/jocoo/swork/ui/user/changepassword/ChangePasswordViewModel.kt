package com.jocoo.swork.ui.user.changepassword

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

data class ChangePasswordState(
    val name: String? = null
) : State

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<ChangePasswordState>(ChangePasswordState()) {
    fun changePassword(oldPwdStr: String, newPwdStr: String) {
        launchAndCollectIn(repo.changePassword(oldPwdStr, newPwdStr)) {
            onSuccess = {
                _changePwdFlow.tryEmit(true)
            }
        }
    }

    private val _changePwdFlow = MutableSharedFlow<Boolean>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val changePwdFlow = _changePwdFlow.asSharedFlow()
}
