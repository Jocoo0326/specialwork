package com.jocoo.swork.work.type

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.bean.WorkTypeInfo
import com.jocoo.swork.data.ApiRepo
import com.jocoo.swork.data.enum.WorkType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class WorkTypeState(
    val list: List<WorkTypeInfo>? = null
) : State {
    override fun equals(other: Any?): Boolean {
        if (other is WorkTypeState) {
            return super.equals(other) && list == other.list
        }
        return super.equals(other)
    }
}

@HiltViewModel
class WorkTypeViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<WorkTypeState>(WorkTypeState()) {

    init {
        val initList = listOf(
            WorkType.Fire,
            WorkType.LimitSpace,
            WorkType.PullingBlocking,
            WorkType.Height,
            WorkType.Lifting,
            WorkType.Electric,
            WorkType.Construction,
            WorkType.Road,
        ).map {
            WorkTypeInfo(it.name, it.id, 0, bgResId = it.bgResId)
        }
        setState {
            it.copy(list = initList)
        }
    }

    fun fetchStatistic(id: Int) {
        launchAndCollectIn(repo.getStatistic()) {
            onSuccess = {
                it.rate_type_total?.firstOrNull { it1 ->
                    it1.id == id
                }?.data?.let { li ->
                    val newList = state.value.list?.toList()
                    li.forEach {
                        newList?.firstOrNull { it1 -> it1.type_id == it.type_id }?.apply {
                            name = it.name
                            total = it.total
                        }
                    }
                    setState { state ->
                        state.copy(list = newList)
                    }
                }
            }
        }
    }
}