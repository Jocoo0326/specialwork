package com.gdmm.core.util

import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

/**
 * 保存图片
 * @param path 图片路径
 * @return 是否写入成功
 */
fun Bitmap.saveBitmap(path: String): Boolean {
    var fos: FileOutputStream? = null
    return try {
        fos = FileOutputStream(File(path))
        compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    } finally {
        fos?.let {
            try {
                it.close()
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
        recycle()
    }
}