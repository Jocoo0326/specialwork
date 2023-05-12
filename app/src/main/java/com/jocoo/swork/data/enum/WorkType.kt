package com.jocoo.swork.data.enum

import androidx.annotation.DrawableRes
import com.jocoo.swork.R

sealed class WorkType(
    var id: Int,
    var name: String,
    @DrawableRes var bgResId: Int,
) {
    companion object {
        const val Fire_Id = 1 //动火作业
        const val LimitSpace_Id = 2 //受限空间作业
        const val PullingBlocking_Id = 3 //盲板抽堵作业
        const val Height_Id = 4 //高处作业
        const val Lifting_Id = 5 //吊装作业
        const val Electric_Id = 6 //临时用电作业
        const val Construction_Id = 7//动土作业
        const val Road_Id = 8 //断路作业
    }


    object Fire : WorkType(Fire_Id, "动火作业", R.drawable.work_type_ic_item_fire)
    object LimitSpace : WorkType(LimitSpace_Id, "受限空间作业", R.drawable.work_type_ic_item_limitspace)
    object PullingBlocking : WorkType(PullingBlocking_Id, "盲板抽堵作业", R.drawable.work_type_ic_item_pullingblocking)
    object Height : WorkType(Height_Id, "高处作业", R.drawable.work_type_ic_item_height)
    object Lifting : WorkType(Lifting_Id, "吊装作业", R.drawable.work_type_ic_item_lifting)
    object Electric : WorkType(Electric_Id, "临时用电作业", R.drawable.work_type_ic_item_power)
    object Construction : WorkType(Construction_Id, "动土作业", R.drawable.work_type_ic_item_constrution)
    object Road : WorkType(Road_Id, "断路作业", R.drawable.work_type_ic_item_blockload)
}