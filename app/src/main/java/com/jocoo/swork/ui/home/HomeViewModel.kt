package com.jocoo.swork.ui.home

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.bean.HomeItem
import com.jocoo.swork.data.ApiRepo
import com.jocoo.swork.data.enum.WorkMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class HomeState(
    val list: List<HomeItem>? = null
) : State {
    override fun equals(other: Any?): Boolean {
        if (other is HomeState) {
            return super.equals(other) && list == other.list
        }
        return super.equals(other)
    }
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<HomeState>(HomeState()) {

    init {
        val initList = listOf(
            HomeItem(
                WorkMode.Todo.name,
                0,
                WorkMode.Todo.id,
                null,
                WorkMode.Todo.bgResId,
                WorkMode.Todo.textColor,
                WorkMode.Todo.dotBgRes,
            ),
            HomeItem(
                WorkMode.Doing.name,
                0,
                WorkMode.Doing.id,
                null,
                WorkMode.Doing.bgResId,
                WorkMode.Doing.textColor,
                WorkMode.Doing.dotBgRes,
            ),
            HomeItem(
                WorkMode.Done.name,
                0,
                WorkMode.Done.id,
                null,
                WorkMode.Done.bgResId,
                WorkMode.Done.textColor,
                WorkMode.Done.dotBgRes,
            ),
        )
        setState { state ->
            state.copy(list = initList)
        }
    }

    fun fetchStatistic() {
        launchAndCollectIn(repo.getStatistic()) {
            onSuccess = {
                val newList = state.value.list?.toList()
                it.rate_type_total?.forEach { item ->
                    newList?.firstOrNull { it1 -> it1.id == item.id }?.apply {
                        name = item.name
                        total = item.total
                        data = item.data
                    }
                }
                setState { state ->
                    state.copy(list = newList)
                }
            }
        }
    }
}