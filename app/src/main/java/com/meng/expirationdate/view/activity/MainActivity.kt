package com.meng.expirationdate.view.activity

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gyf.immersionbar.ImmersionBar
import com.meng.expirationdate.R
import com.meng.expirationdate.base.BaseActivity
import com.meng.expirationdate.databinding.ActivityMainBinding
import com.meng.expirationdate.view.fragment.MainCalendarFragment
import com.meng.expirationdate.view.fragment.MainHomeFragment
import com.meng.expirationdate.view.fragment.MainSettingFragment
import com.meng.expirationdate.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val mViewModel by lazy {
        createVM<MainViewModel>()
    }

    override fun getLayoutId() = R.layout.activity_main

    override fun initView() {
        mBinding.apply {
            vm = mViewModel

            viewpager.adapter = MyAdapter(supportFragmentManager)
            viewpager.offscreenPageLimit = 3

            initTab() //初始化底部栏
        }

        ImmersionBar.with(this@MainActivity)
            .fitsSystemWindows(true)
            .statusBarColor(R.color.white)
            .statusBarDarkFont(true)
            .init()

/*        mBinding.imageSearch.onClickNoAnim {
            //search

        }

        mBinding.imageAdd.onClickNoAnim {
            //add item
        }*/


    }

    override fun initData() {
    }

    private fun initTab() {
        mBinding.tabBottom.itemIconTintList = null
        mBinding.tabBottom.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tab_main -> {
                    mBinding.viewpager.setCurrentItem(0, false)
                }
                R.id.tab_calendar -> {
                    mBinding.viewpager.setCurrentItem(1, false)
                }
                R.id.tab_setting -> {
                    mBinding.viewpager.setCurrentItem(2, false)
                }
            }
            true
        }
    }

    fun getRootView(): ViewGroup {
        return mBinding.clRoot
    }

    class MyAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private var fragments: MutableList<Fragment> = ArrayList()
        private lateinit var mainHomeFragment: MainHomeFragment
        private lateinit var mainCalendarFragment: MainCalendarFragment
        private lateinit var mainSettingFragment: MainSettingFragment

        init {
            initFragment()

            fragments.add(mainHomeFragment) //home page
            fragments.add(mainCalendarFragment) //calendar page
            fragments.add(mainSettingFragment) //setting page
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int = fragments.size

        private fun initFragment() {
            mainHomeFragment = MainHomeFragment()
            mainCalendarFragment = MainCalendarFragment()
            mainSettingFragment = MainSettingFragment()
        }
    }
}