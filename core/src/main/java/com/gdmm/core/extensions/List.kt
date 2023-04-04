package com.gdmm.core.extensions


/**
 * 截取列表
 *
 * @param T
 * @param maxIndex 最大元素个数
 * @return
 */
fun <T> List<T>.limit(maxIndex: Int): List<T> {
    if (size > maxIndex) {
        return subList(0, maxIndex)
    }
    return this
}

fun <T> List<T>?.mutableList(): MutableList<T> {
    return this?.toMutableList() ?: mutableListOf()
}