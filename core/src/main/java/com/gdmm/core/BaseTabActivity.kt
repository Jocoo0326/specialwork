package com.gdmm.core

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.TooltipCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.gdmm.core.databinding.CoreActivityTabLayoutBinding
import com.gdmm.core.extensions.dp2px
import com.gdmm.core.extensions.setupActionBar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator

abstract class BaseTabActivity : BaseActivity<CoreActivityTabLayoutBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar(mBinding.actionBar.toolbar)
        setupTabLayout(mBinding.tabLayout, mBinding.pager)
    }

    open fun setupTabLayout(tabLayout: TabLayout, viewPager: ViewPager2) {

        tabLayout.setupWithViewPager2(
            tabMenus = getTabMenus(),
            viewPager = viewPager,
            onTabSelectedListener = onTabSelectedListener,
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle
        ) { position ->
            createFragment(position)
        }
    }

    open fun setupActionBar(toolbar: Toolbar) {
        setupActionBar(toolbar, title())
    }

    abstract fun title(): String

    abstract fun getTabMenus(): Array<String>

    abstract fun createFragment(position: Int): Fragment

    abstract val onTabSelectedListener: TabLayout.OnTabSelectedListener?

    override fun getViewBinding() = CoreActivityTabLayoutBinding.inflate(layoutInflater)

    override fun onDestroy() {
        super.onDestroy()
        mBinding.tabLayout.clearOnTabSelectedListeners()
    }
}


open class FragmentAdapter(
    var size: Int = 0,
    val fragmentFactory: (Int) -> Fragment,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentFactory.invoke(position)
    }
}

open class SimpleTabSelectedListener : OnTabSelectedListener {
    override fun onTabSelected(tab: Tab) {

    }

    override fun onTabUnselected(tab: Tab) {

    }

    override fun onTabReselected(tab: Tab) {

    }
}

fun Tab.textView() = view.getChildAt(1) as? AppCompatTextView


fun TabLayout.setupWithViewPager2(
    tabMenus: Array<String>,
    viewPager: ViewPager2,
    onTabSelectedListener: OnTabSelectedListener? = null,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    fragmentFactory: (Int) -> Fragment,
) {

    onTabSelectedListener?.let {
        addOnTabSelectedListener(it)
    }

    //PagerAdapter
    FragmentAdapter(
        tabMenus.size, fragmentFactory = fragmentFactory,
        fragmentManager = fragmentManager,
        lifecycle = lifecycle
    ).apply {
        viewPager.adapter = this
    }

    TabLayoutMediator(this, viewPager) { tab, position ->
        tab.text = tabMenus[position]
        TooltipCompat.setTooltipText(tab.view, null)//不显示长按toast
    }.attach()
}

/**
 * TabLayout角标
 *
 * @param position tab 位置
 * @param unReadNum 未读消息数量
 * @param badgeTextColor 角标字体颜色
 * @param badgeBgColor 角标背景颜色
 * @param verticalOffsetWithText 竖向偏移量
 * @param horizontalOffsetWithText 横向偏移量
 */
fun TabLayout.addOrClearBadge(
    position: Int,
    unReadNum: Int = 0,
    badgeTextColor: Int = 0xffa6a7ab.toInt(),
    badgeBgColor: Int = Color.TRANSPARENT,
    verticalOffsetWithText: Int = context.dp2px(6),
    horizontalOffsetWithText: Int = context.dp2px(-2)
) {
    if (position < 0 || position > tabCount - 1) return

    getTabAt(position)?.orCreateBadge?.apply {
        backgroundColor = badgeBgColor
        this.badgeTextColor = badgeTextColor
        this.verticalOffsetWithText = verticalOffsetWithText
        this.horizontalOffsetWithText = horizontalOffsetWithText

        if (unReadNum > 0) {
            number = unReadNum
        } else {
            clearNumber()
        }
    }
}
