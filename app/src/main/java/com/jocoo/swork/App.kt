package com.jocoo.swork

import com.gdmm.core.BaseApplication
import com.hjq.toast.Toaster
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        Toaster.init(this)
    }
}