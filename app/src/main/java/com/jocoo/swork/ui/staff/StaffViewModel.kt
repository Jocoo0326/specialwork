package com.jocoo.swork.ui.staff

import com.blankj.utilcode.util.SPUtils
import com.gdmm.core.BaseApplication
import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.gdmm.core.network.SessionManager
import com.jocoo.swork.data.PREF_KEY_ACCOUNT
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


data class StaffState(
    val name: String? = "",
    val account: String? = ""
) : State

@HiltViewModel
class StaffViewModel @Inject constructor(

) : BaseViewModel<StaffState>(StaffState()) {

    fun fetchUserInfo() {
        val sm = SessionManager.getInstance(BaseApplication.applicationContext())
        val account = SPUtils.getInstance().getString(PREF_KEY_ACCOUNT, "")
        setState { state ->
            state.copy(
                name = sm.userInfo?.org_name,
                account = account
            )
        }
    }
}
