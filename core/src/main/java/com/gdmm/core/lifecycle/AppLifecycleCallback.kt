package com.gdmm.core.lifecycle

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

class AppLifecycleCallback : ActivityLifecycleCallbacks {
    
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ActivityStackManager.addActivity(activity)
    }
    
    override fun onActivityStarted(activity: Activity) {
    
    }
    
    override fun onActivityResumed(activity: Activity) {
    
    }
    
    override fun onActivityPaused(activity: Activity) {
    
    }
    
    override fun onActivityStopped(activity: Activity) {
    
    }
    
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    
    }
    
    override fun onActivityDestroyed(activity: Activity) {
        ActivityStackManager.removeActivity(activity)
    }
    
    
}