package com.jocoo.swork.util

import android.content.Context
import android.content.Intent
import com.gdmm.core.BaseApplication
import com.gdmm.core.network.SessionManager
import com.jocoo.swork.ui.login.LoginActivity

fun Context.reLogin() {
    startActivity(Intent(this, LoginActivity::class.java).also {
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    })
    val sm = SessionManager.getInstance(BaseApplication.applicationContext())
    sm.clearAll()
}
