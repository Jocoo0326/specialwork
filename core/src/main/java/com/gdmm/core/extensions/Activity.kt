package com.gdmm.core.extensions

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gdmm.core.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline fun <reified T : Activity> Activity.launchActivity(bundle: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent)
}

fun AppCompatActivity.addFragment(
    container: ViewGroup,
    fragment: Fragment,
    tag: String? = null,
    allowStateLoss: Boolean = true
) {
    supportFragmentManager.commitTransaction(allowStateLoss) {
        add(container.id, fragment, tag)
    }
}

fun AppCompatActivity.addHideFragment(
    container: ViewGroup,
    allowStateLoss: Boolean = true,
    vararg fragmentList: Fragment
) {
    supportFragmentManager.commitTransaction(allowStateLoss) {
        fragmentList.map { fragment ->
            add(container.id, fragment, fragment::class.java.simpleName).hide(fragment)
        }.toList().last()
    }
}

/**
 * 显示对应tag的fragment
 *
 * @param tag 标签
 */
fun AppCompatActivity.showFragment(
    tag: String
) {
    with(supportFragmentManager) {
        commitTransaction(true) {
            fragments.map { fragment ->
                if (tag == fragment.tag) show(fragment) else hide(fragment)
            }.toList().last()
        }
    }
}

/**
 * Launches a new coroutine and repeats `block` every time the Activity's viewLifecycleOwner
 * is in and out of `minActiveState` lifecycle state.
 */
inline fun AppCompatActivity.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

fun AppCompatActivity.replaceFragment(container: ViewGroup, fragment: Fragment) {
    val tag: String = fragment::class.java.simpleName
    val prevFragment = supportFragmentManager.findFragmentByTag(tag)
    if (prevFragment == null) {
        supportFragmentManager.beginTransaction()
            .add(container.id, fragment, tag)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    } else {
        supportFragmentManager.beginTransaction()
            .show(prevFragment)
            .commitAllowingStateLoss()
    }
}

/**
 * Replace an existing fragment that was added to a container
 *
 * @param container
 * @param fragment
 * @param tag
 * @param allowStateLoss
 */
fun AppCompatActivity.replaceFragment(
    container: ViewGroup,
    fragment: Fragment,
    tag: String? = null,
    allowStateLoss: Boolean = true
) {
    supportFragmentManager.commitTransaction(allowStateLoss) {
        replace(container.id, fragment, tag)
    }
}

/**
 * 隐藏系统状态栏｜导航栏 全屏显示
 * {@see "https://developer.android.com/training/system-ui/immersive"}
 *  @param root 布局根View
 */
fun AppCompatActivity.hideSystemUI(root: View) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, root).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

/**
 * 图片延伸到状态栏
 *
 * @param lightTheme true 状态栏字体白色显示
 */
fun AppCompatActivity.setTranslucentStatusBar(lightTheme: Boolean = true) {
    // 0表示“默认”，1表示“隐藏显示区域”
    val mIsNotchSwitchOpen = Settings.Secure.getInt(
        applicationContext.contentResolver, "display_notch_status", 0
    )

    with(window) {
        addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = Color.TRANSPARENT

        var flags = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags = if (lightTheme) {
                flags and SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            } else {
                flags or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        decorView.systemUiVisibility = flags

        //刘海屏处理
        if (mIsNotchSwitchOpen == 1 || Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return@with
        attributes.layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
    }
}


fun AppCompatActivity.setStatusBar(
    statusBarColor: Int = Color.WHITE,
    darkTheme: Boolean = true,
    isFullscreen: Boolean = true
) {
    window.statusBarColor = statusBarColor
    var flags = SYSTEM_UI_FLAG_LAYOUT_STABLE
    if (isFullscreen) {
        flags = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or flags
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        flags = if (darkTheme) {
            flags or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            flags and SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        window.decorView.systemUiVisibility = flags
    }
}

//获取状态栏的高度
fun AppCompatActivity.getStatusBarHeight(): Int {
    val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        this.resources.getDimensionPixelSize(resourceId)
    } else dp2px(25)
}

fun AppCompatActivity.setupActionBar(
    toolbar: Toolbar,
    title: String,
    elevation: Int = 0,
    bgColor: Int = Color.WHITE,
    onBackClick: (View) -> Unit = { finish() }
) {
    with(toolbar) {//toolbar
        setBackgroundColor(bgColor)
        val mTitleTextView =
            findViewById<TextView>(R.id.toolbar_title)
        mTitleTextView.text = title
        setSupportActionBar(toolbar)
        setNavigationOnClickListener {
            onBackClick(it)
        }
    }

    supportActionBar ?: return
    supportActionBar?.apply {
        this.elevation = elevation.toFloat()
        setDisplayHomeAsUpEnabled(true)
        setDisplayShowTitleEnabled(false)
    }
}