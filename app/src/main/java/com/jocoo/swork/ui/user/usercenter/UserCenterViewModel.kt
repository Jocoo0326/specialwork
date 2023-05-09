package com.jocoo.swork.ui.user.usercenter

import com.blankj.utilcode.util.AppUtils
import com.gdmm.core.BaseApplication
import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.gdmm.core.network.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class UserCenterState(
    val name: String? = null,
    val version: String? = null
) : State

@HiltViewModel
class UserCenterViewModel @Inject constructor(

) : BaseViewModel<UserCenterState>(UserCenterState()) {

    fun fetchBaseInfo() {
        val sm = SessionManager.getInstance(BaseApplication.applicationContext())
        setState { state ->
            state.copy(
                name = sm.userInfo?.org_name,
                version = "v${AppUtils.getAppVersionName()}"
            )
        }
    }
}
