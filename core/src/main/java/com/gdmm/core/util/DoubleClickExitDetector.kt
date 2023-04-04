package com.gdmm.core.util

import android.content.Context
import android.widget.Toast

/**
 * 创建一个双击退出识别器
 * @param context Androdi上下文
 * @param hintMessage 默认提示消息为“再按一次退出程序”
 * @param effectiveIntervalTime 有效间隔时间默认为2000毫秒
 */
class DoubleClickExitDetector constructor(
    private val context: Context?, // 提示消息
    private var hintMessage: String? = DEFAULT_HINT_MESSAGE_CHINA,
    private var effectiveIntervalTime: Int = 2000
) {
    private var lastClickTime: Long = 0 // 上次点击时间

    /**
     * 点击，你需要重写Activity的onBackPressed()方法，先调用此方法，如果返回true就执行父类的onBackPressed()方法来关闭Activity否则不执行
     *
     * @return 当两次点击时间间隔小于有效间隔时间时就会返回true，否则返回false
     */
    fun click(): Boolean {
        val currentTime = System.currentTimeMillis()
        val result = currentTime - lastClickTime < effectiveIntervalTime
        lastClickTime = currentTime
        if (!result) {
            Toast.makeText(context, hintMessage, Toast.LENGTH_SHORT).show()
        }
        return result
    }

    companion object {
        var DEFAULT_HINT_MESSAGE_CHINA = "再按一次退出程序"
    }
}