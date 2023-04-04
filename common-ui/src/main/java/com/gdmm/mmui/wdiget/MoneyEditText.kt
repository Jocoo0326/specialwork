package com.gdmm.mmui.wdiget

import android.content.Context
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.AbsoluteSizeSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.njgdmm.diesel.widget.R
import java.math.BigDecimal
import java.math.RoundingMode

class MoneyEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private val symbolTextSize: Int
    private val baseTextSize: Int

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MoneyEditText, defStyleAttr, 0)
        symbolTextSize = typedArray.getDimensionPixelSize(
            R.styleable.MoneyEditText_comm_ui_symbol_text_size,
            28
        )
        baseTextSize = typedArray.getDimensionPixelSize(
            R.styleable.MoneyEditText_comm_ui_base_text_size,
            40
        )
        typedArray.recycle()
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        val text = text.toString()
        if (text.isNotBlank() && text.startsWith("¥")) {
            if (selStart != text.length) { //不能让光标定位到¥前面
                setSelection(text.length)
            }
        }
    }

    private fun onTextChanged(str1: CharSequence?) {
        if (str1.isNullOrEmpty()) {
            return
        }
        val rmb = "¥"
        var amountStr = str1.toString().trim { it <= ' ' }

        //以.(点)开始自动补0
        if (amountStr.startsWith(".")) {
            amountStr = "0$amountStr"
            setInputText(amountStr)
        }

        //以0开始且不是以0.开始
//        if (amountStr.startsWith("0") && str1 != ".") {
//            amountStr = "$amountStr."
//            setInputText(amountStr)
//        }

        // 最多保留小数点2位
        if (amountStr.contains(".")) {
            amountStr = amountStr.replace("..", ".")

            if (amountStr.length - 1 - amountStr.indexOf(".") > 2) {
                amountStr = amountStr.subSequence(0, amountStr.indexOf(".") + 3).toString()
                setInputText(amountStr)
            }
        }

        // 自动追加人民币符号
        if (!amountStr.startsWith(rmb)) {
            amountStr = rmb + amountStr
            setInputText(amountStr)
        }

        //以0开始的整数，把0去掉
        if (!amountStr.contains(".") && amountStr.startsWith(rmb + "0") && amountStr.length >= 3) {
            amountStr = amountStr.replace("^¥0*".toRegex(), rmb)
            setInputText(amountStr)
        }


        //以¥开始
        if (amountStr.startsWith(rmb) && amountStr.length >= 2) {
            removeTextChangedListener(textWatcher)
            val builder = SpannableStringBuilder(amountStr)
            val begin = amountStr.indexOf(rmb)
            val end = begin + rmb.length

            //字体大小
            builder.setSpan(
                AbsoluteSizeSpan(symbolTextSize),
                begin,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            builder.setSpan(
                AbsoluteSizeSpan(baseTextSize),
                end,
                amountStr.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            text = builder
            addTextChangedListener(textWatcher)
        }

        if (amountStr == rmb) {
            text = null
        }
    }

    private fun setInputText(amountStr: String?) {
        setText(amountStr)
        if (!amountStr.isNullOrEmpty()) {
            setSelection(text!!.length)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s)
        }

        override fun afterTextChanged(s: Editable?) {
            var text: String = s.toString()
            val rmb = "¥"
            if (text.isNotEmpty()) {
                if (text.startsWith(rmb) && text.length >= 2) {
                    if (text.contains(".")) {
                        text = text.replace("..", ".")
                    }
                    if (text.contains(".") && text.length - 1 - text.indexOf(".") > 2) {
                        text = text.subSequence(0, text.indexOf(".") + 3).toString()
                    }
                    val amount = text.substring(1).toDouble()

                    if (!maxAmount.isNullOrEmpty() && maxAmount!!.toBigDecimal().compareTo(BigDecimal(0)) > 0
                        && maxAmount!!.toBigDecimal().compareTo(amount.toBigDecimal()) < 0
                    ) {
                        setText(maxAmount.toString())
                    }

                    mInputAmountChangeListener?.onChangeInputAmount(amount)
                }
            } else {
                mInputAmountChangeListener?.onChangeInputAmount(0.0)
            }
        }
    }

    var maxAmount: String? = null
        set(value) {
            field = if (!value.isNullOrEmpty()) keepTwoDecimal(value.toDouble()) else null
        }

    private fun keepTwoDecimal(amount: Double): String {
        return trimTrailingZero(amount.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toString())
    }

    private fun trimTrailingZero(s: String): String {
        // 不存在小数点，或者含有多个小数点，则不处理
        if (!s.contains(".") || s.indexOf(".") != s.lastIndexOf(".")) return s
        //先去掉末尾的0，再去掉末尾的0
        return if (s.contains(".")) s.replace("0*$".toRegex(), "").replace("\\.$".toRegex(), "") else s
    }

    var mInputAmountChangeListener: InputAmountChangeListener? = null

    fun getAmount(): String {
        val amountStr = text.toString()
        if (amountStr.startsWith("¥"))
            return amountStr.substring(1, amountStr.length)
        return amountStr
    }

    interface InputAmountChangeListener {

        /**
         * 金额变化
         * @param amount 金额
         */
        fun onChangeInputAmount(amount: Double)
    }
}