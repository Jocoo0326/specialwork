package com.gdmm.core.util

import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.squareup.moshi.Moshi


fun SmartRefreshLayout.onLoadCompleted(list: List<*>?, pageSize: Int = 10) {
    finishRefresh()
    if (isEnable4LoadMore(list, pageSize)) finishLoadMore() else finishLoadMoreWithNoMoreData()
}

fun isEnable4LoadMore(list: List<*>?, pageSize: Int = 10): Boolean {
    return list?.isNotEmpty() == true && pageSize > 0 && list.size % pageSize == 0
}

/**
 * 计算列表下一页
 *
 * @param list 当前列表
 * @param pageSize 页容量
 * @return
 */
fun calcNextPage(list: List<*>?, pageSize: Int = 10): Int {
    return ((list?.size ?: 0) + pageSize - 1) / pageSize + 1
}

/**
 * merge 新旧列表
 *
 * @param T
 * @param oldList
 * @param newList
 * @param pageSize
 * @return
 */
fun <T> mergeList(oldList: List<T>?, newList: List<T>?, curPage: Int = 1): List<T> {
    return if (curPage > 1) {
        mutableListOf<T>().apply {
            oldList?.let {
                addAll(it)
            }
            newList?.let {
                addAll(it)
            }
        }.toList()
    } else {
        newList ?: emptyList()
    }
}

inline fun <reified T> toJson(t: T): String {
    val moshi: Moshi = Moshi.Builder().build()
    return moshi.adapter(T::class.java).toJson(t)
}

inline fun <reified T> fromJson(json: String?, clazz: Class<T>): T? {
    json ?: return null
    val moshi: Moshi = Moshi.Builder().build()
    return moshi.adapter(clazz).fromJson(json)
}