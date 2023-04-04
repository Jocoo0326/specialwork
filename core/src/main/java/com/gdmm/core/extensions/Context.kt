package com.gdmm.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.LocationManager
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.alibaba.android.arouter.launcher.ARouter


fun Context.dp2px(dp: Int): Int {
    return (this.resources.displayMetrics.density * dp).toInt()
}

fun Context.getDrawableById(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

fun Context.toColor(@ColorRes colorRes: Int): Int = ContextCompat.getColor(this, colorRes)

/**
 * 从assets文件读出字符串文本
 */
fun Context.readText4Assets(fileName: String) = assets.open(fileName).bufferedReader().use { it.readText() }

/**
 * 获取状态栏的高度
 *
 * @return 状态栏的高度
 */
fun Context.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId <= 0) return 0
    return try {
        return resources.getDimensionPixelSize(resourceId)
    } catch (e: Resources.NotFoundException) {
        0
    }
}

fun Context.getDisplayHeight(): Int {
    val displayMetrics = this.resources.displayMetrics
    return displayMetrics.heightPixels
}

fun Context.getDisplayWidth(): Int {
    val displayMetrics = this.resources.displayMetrics
    return displayMetrics.widthPixels
}

/**
 * show toast
 */
fun Context.showShortToast(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()

/**
 * show toast
 */
fun Context.showShortToast(text: CharSequence) =
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

/**
 * show toast middle
 */
fun Context.showMiddleShortToast(resId: Int) {
    val toast = Toast.makeText(this, resId, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

fun Context.showMiddleShortToast(text: CharSequence) {
    val toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

/**
 * Return the string array associated with a particular resource ID.
 *
 * @param id a particular resource ID
 * @return The string array associated with the resource.
 */
fun Context.getStringArray(@ArrayRes id: Int): Array<String> {
    return resources.getStringArray(id)
}

/**
 * 路由跳转
 *
 * @param path 导航路径
 * @param flag 启动模式
 */
fun Context.navigation(path: String, flag: Int = 0) {
    val postcard = ARouter.getInstance().build(path)
    if (flag > 0) {
        postcard.withFlags(flag)
    }
    postcard.navigation(this)
}

inline fun <reified T : Activity> Context.startActivity(args: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    if (args != null) {
        intent.putExtras(args)
    }
    startActivity(intent)
}

/**
 * 是否有权限
 *
 * @param permissions 对应的权限
 * @return true是,false否
 */
fun Context.hasPermissions(vararg permissions: String): Boolean {
    for (permission in permissions) {
        if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
    }
    return true
}

/**
 * 判断GPS是否有开启
 *
 * @return true 是,false否
 */
fun Context.checkGPSIsOpen(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

private const val PREFERENCES_NAME = "com.njgdmm.bms"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)