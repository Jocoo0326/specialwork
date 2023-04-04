package com.gdmm.core.extensions

import android.content.Context
import android.text.Editable
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.gdmm.core.util.SimpleTextWatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce


/**
 * 文本框输入改变监听
 *
 * @param timeoutMillis 超出此时间响应一次输出结果
 * @return Flow<Editable?>
 */
@OptIn(FlowPreview::class)
fun EditText.afterTextChangedFlow(timeoutMillis: Int = 0): Flow<Editable?> {
    val callbackFlow = callbackFlow {

        val watcher = object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                trySend(s)
            }
        }
        addTextChangedListener(watcher)
        awaitClose { removeTextChangedListener(watcher) }
    }

    if (timeoutMillis > 0) {//背压
        callbackFlow.buffer(Channel.CONFLATED)
            .debounce(timeoutMillis.toLong())
    }
    return callbackFlow
}

fun Editable?.toStringOrEmpty(): String = this?.toString() ?: ""


fun Editable?.toAmount(): Double {
    val result = this?.toString() ?: return 0.0
    if (result.startsWith("¥")) {
        return result.substring(1).toDouble()
    }
    return result.toDouble()
}


/**
 * 调起键盘
 */
fun EditText.openKeyboard() {
    isFocusable = true
    isFocusableInTouchMode = true;
    requestFocus()

    val imm: InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}

/**
 * 关闭键盘
 */
fun EditText.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

/**
 * 手机号格式化
 *
 * @param onAfterTextChanged 手机号输入回调
 */
fun EditText.registerPhoneChangedListener(onAfterTextChanged: ((String) -> Unit)? = null) {

    addTextChangedListener(object : SimpleTextWatcher() {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.isEmpty()) return
            val stringBuilder = StringBuilder()
            for (i in s.indices) {
                if (i != 3 && i != 8 && s[i] == ' ') {
                    continue
                } else {
                    stringBuilder.append(s[i])
                    if ((stringBuilder.length == 4 || stringBuilder.length == 9)
                        && stringBuilder[stringBuilder.length - 1] != ' '
                    ) {
                        stringBuilder.insert(stringBuilder.length - 1, ' ')
                    }
                }
            }
            if (stringBuilder.toString() != s.toString()) {
                var index = start + 1
                if (stringBuilder[start] == ' ') {
                    if (before == 0) {
                        index++
                    } else {
                        index--
                    }
                } else {
                    if (before == 1) {
                        index--
                    }
                }
                setText(stringBuilder.toString())
                setSelection(index)
            }
        }

        override fun afterTextChanged(s: Editable?) {
            onAfterTextChanged?.invoke(s.toString().replace(" ", ""))
        }
    })
}

fun EditText.limitDigits(s: CharSequence, digits: Int = 2) {
    if (s.toString().contains(".")) {
        if (s.length - 1 - s.toString().indexOf(".") > digits) {
            val str = s.toString().subSequence(
                0,
                s.toString().indexOf(".") + digits + 1
            )
            this.setText(str)
            this.setSelection(str.length) //光标移到最后
        }
    }

    //如果"."在起始位置,则起始位置自动补0
    if (s.toString().trim { it <= ' ' }.substring(0) == ".") {
        val str = "0$s"
        this.setText(str)
        this.setSelection(2)
    }

    //如果起始位置为0,且第二位跟的不是".",则无法后续输入

    //如果起始位置为0,且第二位跟的不是".",则无法后续输入
    if (s.toString().startsWith("0")
        && s.toString().trim { it <= ' ' }.length > 1
    ) {
        if (s.toString().substring(1, 2) != ".") {
            this.setText(s.subSequence(0, 1))
            this.setSelection(1)
            return
        }
    }
}

