package com.meng.expirationdate.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meng.expirationdate.utils.StatusBarUtil

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity(), IActivity {
    protected lateinit var mBinding: B

    inline fun <reified T : ViewModel> createVM(): T = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[T::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        super.onCreate(savedInstanceState)
        mBinding.lifecycleOwner = this

        changeStatusBar()
        initView()
        initData()
    }

    protected open fun canChangeStatusBar(): Boolean {
        return true
    }

    open fun changeStatusBar() {
        if (canChangeStatusBar()) {
            StatusBarUtil.setTransparent(this)
            StatusBarUtil.setLightMode(this)
        }
    }
}