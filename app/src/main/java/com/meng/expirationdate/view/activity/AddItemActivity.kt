package com.meng.expirationdate.view.activity

import android.app.DatePickerDialog
import android.text.Editable
import android.text.TextWatcher
import android.widget.DatePicker
import com.blankj.utilcode.util.KeyboardUtils
import com.meng.expirationdate.R
import com.meng.expirationdate.base.BaseActivity
import com.meng.expirationdate.databinding.ActivityAddItemBinding
import com.meng.expirationdate.event.PublicMsgEvent
import com.meng.expirationdate.room.DBManager
import com.meng.expirationdate.room.ItemInfo
import com.meng.expirationdate.room.ItemType
import com.meng.expirationdate.utils.CustomToast
import com.meng.expirationdate.utils.onClickNoAnim
import com.meng.expirationdate.viewmodel.AddItemViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddItemActivity: BaseActivity<ActivityAddItemBinding>(), DatePickerDialog.OnDateSetListener {
    override fun getLayoutId(): Int = R.layout.activity_add_item

    private val mViewModel by lazy {
        createVM<AddItemViewModel>()
    }

    private var clickType = 0 //选择日期点击事件类型：0生产日期 1过期时间
    private lateinit var expirationDate: String //最终过期时间

    override fun initView() {
        mBinding.apply {
            vm = mViewModel
        }

        initClick() //设置点击时事件

        mBinding.etViewTime.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.takeIf { it.isNotBlank() }?.let {
                    //根据保质天数计算最终过期时间
                    expirationDate = addDays(mBinding.tvItemDate.text.toString(), it.toInt())
                }
            }

        })
    }

    override fun initData() {
        val calendar = Calendar.getInstance()
        //生产日期默认为当前日期
        mBinding.tvItemDate.text = String.format(getString(R.string.date_format),
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH))
        //最终保质期默认为当前日期
        expirationDate = mBinding.tvItemDate.text.toString()
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
            CustomToast.showToast(getString(R.string.unavailable_now))
        }

        //设置生产日期
        mBinding.rlDate.onClickNoAnim {
            openDatePickerDialog(0)
        }

        //设置过期时间
        mBinding.rlTime.onClickNoAnim {
            if (mViewModel.dateType.get() == 1) {
                openDatePickerDialog(1) //跳转日历选择过期时间
            }
        }

        mBinding.ivBack.onClickNoAnim {
            finish()
        }

        mBinding.tvSave.onClickNoAnim {
            //保存物品信息
            KeyboardUtils.hideSoftInput(this)
            if (mBinding.etItemName.text.isNullOrBlank()) {
                CustomToast.showToast(getString(R.string.name_cannot_null))
                return@onClickNoAnim
            }
            if (mBinding.etItemNum.text.isNullOrBlank()) {
                CustomToast.showToast(getString(R.string.num_cannot_null))
                return@onClickNoAnim
            }
            if (mViewModel.dateType.get() == 0) {
                if (mBinding.etViewTime.text.isNullOrBlank()) {
                    //输入保质天数为空
                    CustomToast.showToast(getString(R.string.time_cannot_null))
                    return@onClickNoAnim
                } else if (mBinding.etViewTime.text.toString().toInt() <= 0) {
                    //输入保质天数有误
                    CustomToast.showToast(getString(R.string.input_time_wrong))
                    return@onClickNoAnim
                }
            } else if (mBinding.tvItemTime.text.isNullOrBlank()) {
                CustomToast.showToast(getString(R.string.time_cannot_null2))
                return@onClickNoAnim
            }
            DBManager.getItemsDAO()?.insertItem(
                ItemInfo(
                    itemId = System.currentTimeMillis(),
                    itemName = mBinding.etItemName.text.toString(),
                    itemNum = mBinding.etItemNum.text.toString().toInt(),
                    itemDescription = mBinding.etItemRemark.text.toString(),
                    itemType = ItemType.DEFAULT.type,
                    itemProductionDate = mBinding.tvItemDate.text.toString(),
                    itemExpirationDate = expirationDate))
            CustomToast.showToast(getString(R.string.add_success))
            PublicMsgEvent.dataChangeEvent(0) //通知数据更新
            finish()
        }
    }

    /**
     * 打开日期选择弹窗
     * @param type 0生产日期 1过期时间
     * */
    private fun openDatePickerDialog(type: Int) {
        clickType = type
        val calendar = Calendar.getInstance()
        val dialog = DatePickerDialog(this, this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))
        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val dateStr = String.format(getString(R.string.date_format), year, month+1, dayOfMonth)
        if (clickType == 0) {
            //设置生产日期
            mBinding.tvItemDate.text = dateStr
        } else {
            //设置保质日期
            mBinding.tvItemTime.text = dateStr
            expirationDate = dateStr
        }
    }

    /**
     * 根据保质天数计算过期时间
     * @param dateNow 当前生产日期
     * @param days 需要相加的保质期
     * */
    private fun addDays(dateNow: String, days: Int): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        val date = sdf.parse(dateNow)
        date?.let {
            val rightNow = Calendar.getInstance()
            rightNow.time = it
            rightNow.add(Calendar.DAY_OF_YEAR, days)
            return sdf.format(rightNow.time)
        }
        return ""
    }
}