package com.meng.expirationdate.view.fragment

import com.meng.expirationdate.R
import com.meng.expirationdate.base.BaseFragment
import com.meng.expirationdate.databinding.FragmentMainCalendarBinding
import com.meng.expirationdate.viewmodel.MainCalendarViewModel

class MainCalendarFragment: BaseFragment<FragmentMainCalendarBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_main_calendar

    private val mViewModel by lazy {
        createVM<MainCalendarViewModel>()
    }

    override fun initView() {
    }

    override fun initData() {
    }

}