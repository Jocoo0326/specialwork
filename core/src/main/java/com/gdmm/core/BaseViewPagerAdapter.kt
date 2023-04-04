package com.gdmm.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BaseViewPagerAdapter(
    private val size: Int,
    private val fragmentFactory: (Int) -> Fragment,
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    override fun getItemCount() = size

    override fun createFragment(position: Int) = fragmentFactory(position)

}
