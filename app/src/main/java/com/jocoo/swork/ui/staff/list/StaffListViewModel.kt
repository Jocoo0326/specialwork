package com.jocoo.swork.ui.staff.list

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.bean.OperatorInfo
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class StaffListState(
    val list: List<OperatorInfo>? = null
) : State

@HiltViewModel
class StaffListViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<StaffListState>(StaffListState()) {
    fun fetchOperatorList(type: Int, name: String, id: String) {
        val params = mutableMapOf<String, String>()
        params["page"] = "1"
        params["limit"] = "${Int.MAX_VALUE}"
        params["name"] = name
        if (type == 0) {
            params["department_id"] = id
        } else {
            params["contractor_id"] = id
        }
        launchAndCollectIn(repo.getOperatorList(params)) {
            onSuccess = {
                setState { state ->
                    state.copy(list = it.lists)
                }
            }
        }
    }

}