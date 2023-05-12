package com.jocoo.swork.work.list

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.bean.WorkInfo
import com.jocoo.swork.data.ApiRepo
import com.jocoo.swork.data.enum.WorkMode
import com.jocoo.swork.util.PaginationDelegation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class WorkListState(
    val list: List<WorkInfo>? = null,
    val curPage: Int = 1
) : State

@HiltViewModel
class WorkListViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<WorkListState>(WorkListState()) {

    fun getTicketList(workMode: Int, type_id: Int, curPage: Int = 1) {
        val params = mutableMapOf<String, String>()
        params["pageType"] = when (workMode) {
            WorkMode.Todo_Id -> "audit"
            WorkMode.Doing_Id -> "working"
            else -> "finish"
        }
        params["type_id"] = "$type_id"
        params["page"] = "$curPage"
        params["limit"] = "${PaginationDelegation.DEFAULT_PAGE_SIZE}"
        launchAndCollectIn(repo.getTicketList(params)) {
            onSuccess = {
                setState { state ->
                    state.copy(list = it.lists, curPage = curPage)
                }
            }
        }
    }
}
