package com.jocoo.swork.ui.main

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.gdmm.core.BaseActivity
import com.jocoo.swork.R
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.databinding.ActivityMainBinding
import com.jocoo.swork.ui.device.DeviceFragment
import com.jocoo.swork.ui.home.HomeFragment
import com.jocoo.swork.ui.monitor.MonitorFragment
import com.jocoo.swork.ui.staff.StaffFragment

@Route(path = NavHub.MAIN_INDEX, name = "")
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun isDoubleClickExit(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private val fragmentsMap = mapOf(
        Pair(0, HomeFragment()),
        Pair(1, DeviceFragment()),
        Pair(2, MonitorFragment()),
        Pair(3, StaffFragment()),
    )
    private val idMap = mapOf(
        Pair(R.id.menu_home, 0),
        Pair(R.id.menu_device, 1),
        Pair(R.id.menu_monitor, 2),
        Pair(R.id.menu_staff, 3),
    )

    private fun initView() {
        mBinding.viewPager.apply {
            isUserInputEnabled = false
            adapter = MainViewPagerAdapter(this@MainActivity, fragmentsMap)
        }
        mBinding.bottomNav.setOnItemSelectedListener {
            mBinding.viewPager.setCurrentItem(idMap[it.itemId]!!, false)
            return@setOnItemSelectedListener true
        }
    }

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)
}