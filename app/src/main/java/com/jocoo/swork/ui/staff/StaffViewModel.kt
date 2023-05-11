package com.jocoo.swork.ui.staff

import com.blankj.utilcode.util.SPUtils
import com.gdmm.core.BaseApplication
import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.gdmm.core.network.SessionManager
import com.jocoo.swork.bean.StaffGroupItem
import com.jocoo.swork.bean.WorkUnitItem
import com.jocoo.swork.data.ApiRepo
import com.jocoo.swork.data.PREF_KEY_ACCOUNT
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


data class StaffState(
    val name: String? = "",
    val account: String? = "",
    val staffGroupList: List<StaffGroupItem>? = null,
    val treeList: List<List<WorkUnitItem>?>? = null
) : State {
    override fun equals(other: Any?): Boolean {
        if (other is StaffState) {
            return super.equals(other)
                && staffGroupList == other.staffGroupList
                && treeList == other.treeList
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
                it.lists?.forEach { item ->
                    item.isDepartment = true
                }
                val staffList = state.value.staffGroupList?.toList()
                staffList?.getOrNull(0)?.itemSublist = it.lists
                setState { state ->
                    state.copy(staffGroupList = staffList)
                }
            }
        }
    }

    fun fetchDepartmentList2(parentId: String = "0") {
        launchAndCollectIn(repo.getDepartmentList(1, Int.MAX_VALUE, parentId)) {
            onSuccess = sc@{
                if (parentId == "0") {
                    setState { state ->
                        state.copy(treeList = listOf(it.lists))
                    }
                    return@sc
                }
                val dtl = state.value.treeList?.toList()
                val levelIndex = dtl?.indexOfFirst { item ->
                    (item?.indexOfFirst { ci ->
                        ci.id == parentId
                    } ?: -1) > -1
                } ?: -1
                if (levelIndex > -1 && !dtl.isNullOrEmpty() && dtl.size > levelIndex) {
                    val newDtl = dtl.take(levelIndex + 1).toMutableList()
                    if (!it.lists.isNullOrEmpty()) {
                        newDtl.add(it.lists)
                    }
                    setState { state ->
                        state.copy(treeList = newDtl)
                    }
                }
            }
        }
    }

    private fun fetchContractorList() {
        launchAndCollectIn(repo.getContractorList(1, Int.MAX_VALUE)) {
            onSuccess = {
                it.lists?.forEach { item ->
                    item.isDepartment = false
                }
                val staffList = state.value.staffGroupList?.toList()
                staffList?.getOrNull(1)?.itemSublist = it.lists
                setState { state ->
                    state.copy(staffGroupList = staffList)
                }
            }
        }
    }

    fun changeCurDepartment(lists: List<WorkUnitItem>?) {
        lists?.forEach { item ->
            item.isDepartment = true
        }
        val staffList = state.value.staffGroupList?.toList()
        staffList?.getOrNull(0)?.itemSublist = lists
        setState { state ->
            state.copy(staffGroupList = staffList)
        }
    }

}
