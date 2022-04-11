package com.meng.expirationdate.view.fragment

import com.meng.expirationdate.R
import com.meng.expirationdate.base.BaseFragment
import com.meng.expirationdate.databinding.FragmentMainSettingBinding
import com.meng.expirationdate.viewmodel.MainSettingViewModel

class MainSettingFragment: BaseFragment<FragmentMainSettingBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_main_setting

    private val mViewModel by lazy {
        createVM<MainSettingViewModel>()
    }

    override fun initView() {
    }

    override fun initData() {
    }

}