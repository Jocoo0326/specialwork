package com.gdmm.mmui.wdiget

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.njgdmm.diesel.widget.R

/**
 * @author Triple
 * @date 2023/2/20
 */
class RequiredTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        val textStr = text.toString()
        val spanString = SpannableString(textStr)
        val matchStr = "*";
        var start = textStr.indexOf(matchStr)
        var end = start + matchStr.length

        if (start in 0..end) {
            //文本高亮
            spanString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.comm_ui_badge_color)),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        text = spanString
    }

}