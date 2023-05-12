package com.jocoo.swork.util

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class PaginationDelegation<T, VH : BaseViewHolder, ADAPTER>(
    private val adapter: ADAPTER,
    private val loadMoreBlock: (nextPage: Int) -> Unit
) where ADAPTER : BaseQuickAdapter<T, VH>, ADAPTER : LoadMoreModule {
    companion object {
        const val DEFAULT_PAGE_SIZE = 10
    }

    private var _curPage: Int = 1

    init {
        adapter.loadMoreModule.apply {
            setOnLoadMoreListener {
                loadMoreBlock(_curPage + 1)
            }
        }
    }

    fun reset() {
        _curPage = 1
        adapter.loadMoreModule.apply {
            loadMoreComplete()
            isAutoLoadMore = true
        }
    }

    fun loadDataForPagination(list: List<T>?, page: Int, isDiffData: Boolean = false) {
        if (isDiffData) {
            adapter.setDiffNewData(list = list?.toMutableList())
        } else {
            if (page == 1) {
                adapter.setList(list?.toMutableList())
            } else {
                list?.let { adapter.addData(it) }
            }
        }
        adapter.loadMoreModule.apply {
            loadMoreComplete()
            val fullPage = (list?.size ?: 0) >= DEFAULT_PAGE_SIZE
            isAutoLoadMore = fullPage
            if (!fullPage) {
                loadMoreEnd()
            }
        }
        _curPage = page
    }
}