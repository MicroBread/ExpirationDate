package com.meng.expirationdate.base

import androidx.annotation.LayoutRes

/***
 * Activity/Fragment的接口
 */

interface IActivity {
    @LayoutRes
    fun getLayoutId(): Int

    fun initView()

    fun initData()

}