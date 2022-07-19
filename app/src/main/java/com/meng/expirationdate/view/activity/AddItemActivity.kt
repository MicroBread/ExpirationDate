package com.meng.expirationdate.view.activity

import android.app.DatePickerDialog
import android.widget.DatePicker
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.meng.expirationdate.R
import com.meng.expirationdate.base.BaseActivity
import com.meng.expirationdate.databinding.ActivityAddItemBinding
import com.meng.expirationdate.utils.onClickNoAnim
import com.meng.expirationdate.viewmodel.AddItemViewModel
import java.util.*

class AddItemActivity: BaseActivity<ActivityAddItemBinding>(), DatePickerDialog.OnDateSetListener {
    override fun getLayoutId(): Int = R.layout.activity_add_item

    private val mViewModel by lazy {
        createVM<AddItemViewModel>()
    }

    private var clickType = 0 //0生产日期 1保质期

    override fun initView() {
        mBinding.apply {
            vm = mViewModel
        }

        initClick() //设置点击时事件
    }

    override fun initData() {
        val calendar = Calendar.getInstance()
        mBinding.tvItemDate.text = String.format(getString(R.string.date_format),
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH)+1,
            calendar.get(Calendar.DAY_OF_MONTH))
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

        //设置生产日期
        mBinding.rlDate.onClickNoAnim {
            openDatePickerDialog(0)
        }

        //设置过期时间
        mBinding.rlTime.onClickNoAnim {
            if (mViewModel.dateType.get() == 1) {
                //跳转日历选择过期时间
                openDatePickerDialog(1)
            }
        }

        mBinding.ivBack.onClickNoAnim {
            finish()
        }

        mBinding.tvSave.onClickNoAnim {
            //todo 保存物品信息
            KeyboardUtils.hideSoftInput(this)
            if (mBinding.etItemName.text.isNullOrEmpty()) {
                ToastUtils.showShort(getString(R.string.name_cannot_null))
                return@onClickNoAnim
            }
            if (mBinding.etItemNum.text.isNullOrEmpty()) {
                ToastUtils.showShort(getString(R.string.num_cannot_null))
                return@onClickNoAnim
            }
            if (mViewModel.dateType.get() == 0) {
                if (mBinding.etViewTime.text.isNullOrEmpty()) {
                    //输入保质天数为空
                    ToastUtils.showShort(getString(R.string.time_cannot_null))
                    return@onClickNoAnim
                } else if (mBinding.etViewTime.text.toString().toInt() <= 0) {
                    //输入保质天数有误
                    ToastUtils.showShort(getString(R.string.input_time_wrong))
                    return@onClickNoAnim
                }
            } else if (mBinding.tvItemTime.text.isNullOrEmpty()) {
                ToastUtils.showShort(getString(R.string.time_cannot_null2))
                return@onClickNoAnim
            }
        }
    }

    private fun openDatePickerDialog(type: Int) {
        clickType = type
        //打开日期选择弹窗
        val calendar = Calendar.getInstance()
        val dialog = DatePickerDialog(this, this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))
        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        if (clickType == 0) {
            //设置生产日期
            mBinding.tvItemDate.text = String.format(getString(R.string.date_format), year, month+1, dayOfMonth)
        } else {
            //设置保质日期
            mBinding.tvItemTime.text = String.format(getString(R.string.date_format), year, month+1, dayOfMonth)
        }
    }
}