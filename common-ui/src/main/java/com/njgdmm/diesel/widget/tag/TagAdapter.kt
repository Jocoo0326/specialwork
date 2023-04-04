package com.njgdmm.diesel.widget.tag

import android.content.Context
import android.widget.ArrayAdapter

/**
 * 标签Adapter
 *
 * @param T
 * @constructor
 *
 * @param context
 * @param layoutId
 */
open class TagAdapter<T>(context: Context, layoutId: Int) : ArrayAdapter<T>(context, layoutId) {
    
    /**
     * 设置数据集
     *
     * @param items
     */
    fun setData(items: List<T>) {
        clear()
        addAll(items)
    }
}