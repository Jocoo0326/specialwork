package com.jocoo.swork.ui.login

import android.content.Context
import com.blankj.utilcode.util.SPUtils
import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.gdmm.core.network.SessionManager
import com.jocoo.swork.data.ApiRepo
import com.jocoo.swork.data.PREF_KEY_ACCOUNT
import com.jocoo.swork.data.PREF_KEY_PWD
import com.jocoo.swork.data.PREF_KEY_SAVE_PWD
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

data class LoginState(
    val account: String = "",
    val pwd: String = "",
    val savePwd: Boolean = false
) : State

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: ApiRepo,
    @ApplicationContext private val appContext: Context
) : BaseViewModel<LoginState>(LoginState()) {

    fun fetchLastAccountInfo() {
        val account = SPUtils.getInstance().getString(PREF_KEY_ACCOUNT, "")
        val pwd = SPUtils.getInstance().getString(PREF_KEY_PWD, "")
        val savePwd = SPUtils.getInstance().getBoolean(PREF_KEY_SAVE_PWD, false)
        setState { state ->
            state.copy(account = account, pwd = pwd, savePwd = savePwd)
        }
    }

    private fun saveAccountInfo(account: String, pwd: String, savePwd: Boolean = false) {
        SPUtils.getInstance().put(PREF_KEY_ACCOUNT, account)
        SPUtils.getInstance().put(PREF_KEY_SAVE_PWD, savePwd)
        SPUtils.getInstance().put(PREF_KEY_PWD, if (savePwd) pwd else "")
    }

    fun login(account: String, pwd: String, savePwd: Boolean = false) {
        launchAndCollectIn(repo.login(account, pwd)) {
            onSuccess = {
                saveAccountInfo(account, pwd, savePwd)
                val sm = SessionManager.getInstance(appContext)
                sm.authToken = it.token
                sm.userInfo = it.user_info
                _loginFlow.tryEmit(true)
            }
        }
    }

    private val _loginFlow = MutableSharedFlow<Boolean>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val loginFlow = _loginFlow.asSharedFlow()
}