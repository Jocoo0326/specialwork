package com.jocoo.swork.data.enum

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.jocoo.swork.R

sealed class WorkMode(
    var id: Int,
    var name: String,
    @DrawableRes var bgResId: Int,
    @ColorInt var textColor: Int,
    @DrawableRes var dotBgRes: Int
) {

    companion object {
        const val Todo_Id = 1
        const val Doing_Id = 20
        const val Done_Id = 99
        fun parseWorkMode(id: Int): WorkMode {
            return when (id) {
                Todo_Id -> Todo
                Doing_Id -> Doing
                Done_Id -> Done
                else -> Done
            }
        }
    }

    object Todo : WorkMode(
        Todo_Id, "待审批作业票", R.drawable.home_bg_item1, Color.WHITE, R.drawable.shape_dot_red
    )

    object Doing : WorkMode(
        Doing_Id, "进行中作业票", R.drawable.home_bg_item1, Color.WHITE, R.drawable.shape_dot_red
    )

    object Done : WorkMode(
        Done_Id, "已完结作业票", R.drawable.home_bg_item2, Color.BLACK, R.drawable.shape_dot_grey
    )
}
