package com.gdmm.core

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.facebook.stetho.Stetho
import com.gdmm.core.lifecycle.AppLifecycleCallback
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout


abstract class BaseApplication : MultiDexApplication() {
    init {
        this.also { instance = it }
    }

    companion object {
        private lateinit var instance: BaseApplication
        fun applicationContext(): Context {
            return instance.applicationContext
        }

        fun instance(): Application = instance
    }

    override fun onCreate() {
        super.onCreate()
        initARouter()
        initRefreshLayout()
        registerActivityLifecycleCallbacks()
        Stetho.initializeWithDefaults(this)
    }

    private fun initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }

    private fun initRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }
    }

    private fun registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(AppLifecycleCallback())
    }
}