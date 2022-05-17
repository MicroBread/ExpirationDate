package com.meng.expirationdate.view.activity

import com.blankj.utilcode.util.ToastUtils
import com.meng.expirationdate.R
import com.meng.expirationdate.base.BaseActivity
import com.meng.expirationdate.databinding.ActivityAddItemBinding
import com.meng.expirationdate.utils.onClickNoAnim
import com.meng.expirationdate.viewmodel.AddItemViewModel

class AddItemActivity: BaseActivity<ActivityAddItemBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_add_item

    private val mViewModel by lazy {
        createVM<AddItemViewModel>()
    }

    override fun initView() {
        mBinding.apply {
            vm = mViewModel
        }

        initClick() //设置点击时事件
    }

    override fun initData() {
    }

    private fun initClick() {
        //设置过期时间计算方式为保质天数
        mBinding.tvDateType0.onClickNoAnim {
            if (mViewModel.dateType.get() != 0) {
                mViewModel.dateType.set(0)
            }
        }

        //设置过期时间计算方式为过期日期
        mBinding.tvDateType1.onClickNoAnim {
            if (mViewModel.dateType.get() != 1) {
                mViewModel.dateType.set(1)
            }
        }

        //todo 产品分类
        mBinding.rlSort.onClickNoAnim {
            ToastUtils.showShort(getString(R.string.unavailable_now))
        }

        //设置过期时间
        mBinding.rlTime.onClickNoAnim {
            ToastUtils.showShort(getString(R.string.unavailable_now))
            if (mViewModel.dateType.get() == 0) {
                //todo 跳转设置保质天数
            } else {
                //todo 跳转设置过期时间
            }
        }

        mBinding.ivBack.onClickNoAnim {
            finish()
        }

        mBinding.tvSave.onClickNoAnim {
            //todo 保存物品信息
        }
    }
}