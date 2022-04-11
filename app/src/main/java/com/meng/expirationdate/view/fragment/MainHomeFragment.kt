package com.meng.expirationdate.view.fragment

import android.util.Log
import android.widget.Toast
import com.blankj.utilcode.util.ToastUtils
import com.meng.expirationdate.R
import com.meng.expirationdate.base.BaseFragment
import com.meng.expirationdate.databinding.FragmentMainHomeBinding
import com.meng.expirationdate.utils.CustomToast
import com.meng.expirationdate.utils.onClickNoAnim
import com.meng.expirationdate.viewmodel.MainHomeViewModel

class MainHomeFragment: BaseFragment<FragmentMainHomeBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_main_home

    private val mViewModel by lazy {
        createVM<MainHomeViewModel>()
    }

    override fun initView() {
        mBinding.imageSearch.onClickNoAnim {
            CustomToast.showToast(it.context, "搜索页待完成")
        }

        mBinding.imageAdd.onClickNoAnim {
            CustomToast.showToast(it.context, "添加条目")
        }
    }

    override fun initData() {
    }

}