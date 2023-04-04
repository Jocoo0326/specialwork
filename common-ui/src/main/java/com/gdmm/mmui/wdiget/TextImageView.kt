package com.gdmm.mmui.wdiget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

open class TextImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    
    init {
        val drawable = compoundDrawables[2]
        drawable?.setBounds(0, 3, drawable.intrinsicWidth, 3 + drawable.intrinsicHeight)
        setCompoundDrawables(null, null, drawable, null)
    }
}