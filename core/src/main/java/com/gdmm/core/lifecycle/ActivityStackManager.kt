package com.gdmm.core.lifecycle

import android.app.Activity

object ActivityStackManager {
    
    private val mActivityList = mutableListOf<Activity>()
    
    /**
     * 获取最近启动的一个Activity
     *
     * @return
     */
    val topActivity: Activity?
        get() {
            return mActivityList.lastOrNull()
        }
    
    fun addActivity(activity: Activity) {
        synchronized(ActivityStackManager::class.java) {
            mActivityList.add(activity)
        }
    }
    
    fun removeActivity(activity: Activity) {
        synchronized(ActivityStackManager::class.java) {
            if (mActivityList.contains(activity)) {
                mActivityList.remove(activity)
            }
        }
    }
    
    /**
     * finish 最后一个之外的所有activity
     */
    fun removeActivityListExcludeLast() {
        if (mActivityList.isNotEmpty()) {
            removeActivityListExcludeAtPosition(mActivityList.size - 1)
        }
    }
    
    fun removeActivityListExcludeAtPosition(position: Int) {
        synchronized(ActivityStackManager::class.java) {
            val list = mActivityList.filterIndexed { index, _ -> index != position }.toMutableList()
            val iterator = list.listIterator()
            while (iterator.hasNext()) {
                val activity = iterator.next()
                iterator.remove()
                activity.finish()
            }
        }
    }
    
    fun killAll() {
        synchronized(ActivityStackManager::class.java) {
            val iterator = mActivityList.listIterator()
            while (iterator.hasNext()) {
                val activity = iterator.next()
                iterator.remove()
                activity.finish()
            }
        }
    }
    
}