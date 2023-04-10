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
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseCompatActivity<ActivitySplashBinding, SplashState, SplashViewModel>() {

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

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun bindListener() {
        viewModel.state.observeWithLifecycle(this, minActiveState = Lifecycle.State.CREATED) {
            if (it.countDownSeconds == 0) {
                rp = rxPermission.request(*permissions)
                    .subscribe { permitted ->
                        if (permitted) {
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            startActivity(Intent(this, LoginActivity::class.java))
                            Toaster.show("权限获取失败，请手动获取")
                            finish()
                        }
                    }
//                ARouter.getInstance().build(NavHub.MAIN).navigation()
            }
            mBinding.tvCd.text = "(${it.countDownSeconds})"
        }
        viewModel.countDown()
    }

    override fun onDestroy() {
        super.onDestroy()
        rp.dispose()
    }

    override fun onViewStateChange(state: SplashState) {

    }

    override fun getViewBinding() = ActivitySplashBinding.inflate(layoutInflater)
}