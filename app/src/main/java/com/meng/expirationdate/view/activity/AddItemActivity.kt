package com.meng.expirationdate.view.activity

import com.meng.expirationdate.R
import com.meng.expirationdate.base.BaseActivity
import com.meng.expirationdate.databinding.ActivityAddItemBinding
import com.meng.expirationdate.utils.onClickNoAnim

class AddItemActivity: BaseActivity<ActivityAddItemBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_add_item

    override fun initView() {
        mBinding.ivBack.onClickNoAnim {
            finish()
        }

        mBinding.tvSave.onClickNoAnim {
            //保存物品信息
        }
    }

    override fun initData() {
    }
}