package com.gdmm.mmui.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.gdmm.mmui.util.stateListDrawable

/**
 * 多个状态背景 对应selector.xml
 */
fun ImageView.setImageSrc(
    @DrawableRes normalId: Int,
    @DrawableRes pressedId: Int? = null,
    @DrawableRes selectedId: Int? = null,
    @DrawableRes checkedId: Int? = null,
    @DrawableRes enabledId: Int? = null,
    @DrawableRes disabledId: Int? = null
) {

    val drawable = stateListDrawable(
        getDrawable(normalId)!!,
        getDrawable(pressedId),
        getDrawable(selectedId),
        getDrawable(checkedId),
        getDrawable(enabledId),
        getDrawable(disabledId)
    )
    setImageDrawable(drawable)
}