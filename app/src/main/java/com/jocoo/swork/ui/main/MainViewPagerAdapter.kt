package com.jocoo.swork.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPagerAdapter(
    activity: FragmentActivity,
    private val fragmentsMap: Map<Int, Fragment>
) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return fragmentsMap.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentsMap[position]!!
    }

}
