package com.gdmm.mmui.wdiget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.njgdmm.diesel.widget.R

/**
 * 带清除功能的EditText
 */
class ClearEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr), OnFocusChangeListener,
    TextWatcher {

    private val mClearTextIcon: Drawable =
        compoundDrawables[2] ?: ContextCompat.getDrawable(context, R.drawable.common_ui_ic_clear)!!

    var textWatcher: TextWatcher? = null
    var mOnFocusChangeListener: OnFocusChangeListener? = null


    init {
        mClearTextIcon.setBounds(
            0,
            0,
            mClearTextIcon.intrinsicHeight,
            mClearTextIcon.intrinsicHeight
        )

        setClearIconVisible(false)
        onFocusChangeListener = this
        addTextChangedListener(this)
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (hasFocus) {
            setClearIconVisible(text?.isNotEmpty() == true)
        } else {
            setClearIconVisible(false)
        }
        mOnFocusChangeListener?.onFocusChange(view, hasFocus)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                text = null
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (isFocused) {
            setClearIconVisible(s.isNotEmpty())
        }
        textWatcher?.onTextChanged(s, start, before, count)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        textWatcher?.beforeTextChanged(s, start, count, after)
    }

    override fun afterTextChanged(s: Editable) {
        textWatcher?.afterTextChanged(s)
    }

    private fun setClearIconVisible(visible: Boolean) {
        mClearTextIcon.setVisible(visible, false)
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1],
            if (visible) mClearTextIcon else null,
            compoundDrawables[3]
        )
    }
}