package com.gdmm.core.extensions

import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible

fun TextView.setTextOrHide(
    newText: CharSequence?,
    hideWhenBlank: Boolean = true,
    vararg relatedViews: View = emptyArray()
) {
    if (newText == null ||
        (newText.isBlank() && hideWhenBlank)
    ) {
        isVisible = false
        relatedViews.forEach { it.isVisible = false }
    } else {
        this.text = newText
        isVisible = true
        relatedViews.forEach { it.isVisible = true }
    }
}

/**
 * TextView 文本高亮
 *
 * @param content textview 全部内容
 * @param highlightColor4Text 高亮文本(颜色高亮)
 * @param highlightTextColor 高亮颜色
 * @param highlightSize4Text 高亮文本(大小高亮)
 * @param highlightTextSize 高亮文本的大小
 */
fun TextView.makeHighlight(
    content: String,
    highlightColor4Text: String? = null,
    highlightTextColor: Int = 0,
    highlightSize4Text: String? = null,
    highlightTextSize: Int = 0
) {
    val spanString = SpannableString(content)
    var start = content.indexOf(highlightColor4Text ?: "")
    var end = start + (highlightColor4Text?.length ?: -1)

    if (start in 0..end && highlightTextColor != 0) {
        //文本高亮
        spanString.setSpan(
            ForegroundColorSpan(highlightTextColor),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    //大小高亮
    start = content.indexOf(highlightSize4Text ?: "")
    end = start + (highlightSize4Text?.length ?: -1)
    if (start in 0..end && highlightTextSize > 0) {
        spanString.setSpan(
            AbsoluteSizeSpan(highlightTextSize),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    text = spanString
}

/**
 * TextView超链接点击
 *
 * @param links key超链接文本,value点击事件
 */
fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    var startIndexOfLink = -1
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {

            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.color = textPaint.linkColor
                // toggle below values to enable/disable
                // the underline shown below the clickable text
                textPaint.isUnderlineText = false

            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
//      if(startIndexOfLink == -1) continue // todo if you want to verify your texts contains links text
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

/**
 * 文本输入监听
 *
 * @param beforeTextChanged
 * @param onTextChanged
 * @param afterTextChanged
 * @return
 */
inline fun TextView.addTextChangedListener(
    crossinline beforeTextChanged: (
        text: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) -> Unit = { _, _, _, _ -> },
    crossinline onTextChanged: (
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) -> Unit = { _, _, _, _ -> },
    crossinline afterTextChanged: (text: Editable?) -> Unit = {}
): TextWatcher {

    val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged.invoke(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(s, start, before, count)
        }

        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s)
        }
    }
    addTextChangedListener(textWatcher)
    return textWatcher
}

fun TextView.setMargins(l: Int = 0, t: Int = 0, r: Int = 0, b: Int = 0) {
    (this.layoutParams as ViewGroup.MarginLayoutParams).setMargins(l, t, r, b)
}

/**
 * 字体加粗
 * @param style Typeface.BOLD,Typeface.NORMAL
 */
fun TextView.typeface(style: Int) {
    setTypeface(null, style)
}

