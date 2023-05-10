package com.jocoo.swork.ui.staff

import com.blankj.utilcode.util.SPUtils
import com.gdmm.core.BaseApplication
import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.gdmm.core.network.SessionManager
import com.jocoo.swork.bean.StaffGroupItem
import com.jocoo.swork.data.ApiRepo
import com.jocoo.swork.data.PREF_KEY_ACCOUNT
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


data class StaffState(
    val name: String? = "",
    val account: String? = "",
    val staffGroupList: List<StaffGroupItem>? = null
) : State {
    override fun equals(other: Any?): Boolean {
        if (other is StaffState) {
            return super.equals(other) && staffGroupList == other.staffGroupList
        }
        return super.equals(other)
    }
}

@HiltViewModel
class StaffViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<StaffState>(StaffState()) {

    init {
        setState {
            it.copy(
                staffGroupList =
                mutableListOf(StaffGroupItem("公司部门"), StaffGroupItem("承包商"))
            )
        }
    }

    fun fetchUserInfo() {
        val sm = SessionManager.getInstance(BaseApplication.applicationContext())
        val account = SPUtils.getInstance().getString(PREF_KEY_ACCOUNT, "")
        setState { state ->
            state.copy(
                name = sm.userInfo?.org_name, account = account
            )
        }
    }

    fun fetchData() {
        fetchDepartmentList()
        fetchContractorList()
    }

    fun fetchDepartmentList(parentId: String = "0") {
        launchAndCollectIn(repo.getDepartmentList(1, Int.MAX_VALUE, parentId)) {
            onSuccess = {
                val staffList = state.value.staffGroupList?.toList()
                staffList?.getOrNull(0)?.itemSublist = it.lists
                setState { state ->
                    state.copy(staffGroupList = staffList)
                }
            }
        }
    }

    fun fetchContractorList() {
        launchAndCollectIn(repo.getContractorList(1, Int.MAX_VALUE)) {
            onSuccess = {
                val staffList = state.value.staffGroupList?.toList()
                staffList?.getOrNull(1)?.itemSublist = it.lists
                setState { state ->
                    state.copy(staffGroupList = staffList)
                }
            }
        }
    }

}
