package com.jocoo.swork.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import com.gdmm.core.BaseCompatActivity
import com.gdmm.core.extensions.observeWithLifecycle
import com.hjq.toast.Toaster
import com.jocoo.swork.databinding.ActivitySplashBinding
import com.jocoo.swork.ui.login.LoginActivity
import com.tbruyelle.rxpermissions3.RxPermissions
import com.trello.rxlifecycle4.android.ActivityEvent
import com.trello.rxlifecycle4.android.RxLifecycleAndroid
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseCompatActivity<ActivitySplashBinding, SplashState, SplashViewModel>() {
    private val behaviorSubject: BehaviorSubject<ActivityEvent> by lazy {
        BehaviorSubject.create()
    }

    private lateinit var rp: Disposable
    override val viewModel: SplashViewModel by viewModels()

    private val rxPermission by lazy {
        RxPermissions(this)
    }
    private val permissions = arrayOf(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.CAMERA,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        behaviorSubject.onNext(ActivityEvent.CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        behaviorSubject.onNext(ActivityEvent.DESTROY)
    }

    override fun onPause() {
        super.onPause()
        behaviorSubject.onNext(ActivityEvent.PAUSE)
    }

    override fun onResume() {
        super.onResume()
        behaviorSubject.onNext(ActivityEvent.RESUME)
    }

    override fun onStart() {
        super.onStart()
        behaviorSubject.onNext(ActivityEvent.START)
    }

    override fun onStop() {
        super.onStop()
        behaviorSubject.onNext(ActivityEvent.STOP)
    }

    override fun initView(savedInstanceState: Bundle?) {
        rp = rxPermission.request(*permissions)
            .compose(RxLifecycleAndroid.bindActivity(behaviorSubject))
            .subscribe { permitted ->
                if (!permitted) {
                    Toaster.show("权限获取失败，请手动获取")
                }
                viewModel.countDown()
            }
    }

    override fun bindListener() {
        viewModel.state.observeWithLifecycle(this, minActiveState = Lifecycle.State.CREATED) {
            if (it.countDownSeconds == 0) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            mBinding.tvCd.text = "(${it.countDownSeconds})"
        }
    }

    override fun onViewStateChange(state: SplashState) {

    }

    override fun getViewBinding() = ActivitySplashBinding.inflate(layoutInflater)
}