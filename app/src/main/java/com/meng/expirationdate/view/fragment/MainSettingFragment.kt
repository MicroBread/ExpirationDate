package com.meng.expirationdate.view.fragment

import com.meng.expirationdate.R
import com.meng.expirationdate.base.BaseFragment
import com.meng.expirationdate.databinding.FragmentMainSettingBinding
import com.meng.expirationdate.event.PublicMsgEvent
import com.meng.expirationdate.room.DBManager
import com.meng.expirationdate.utils.CustomToast
import com.meng.expirationdate.utils.onClickNoAnim
import com.meng.expirationdate.viewmodel.MainSettingViewModel
import com.meng.expirationdate.widget.AlertMsgDialog

/**
 * 首页-设置页
 * */
class MainSettingFragment: BaseFragment<FragmentMainSettingBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_main_setting

    private val mViewModel by lazy {
        createVM<MainSettingViewModel>()
    }

    override fun initView() {
        mBinding.rlClear.onClickNoAnim {
            //删除所有数据
            AlertMsgDialog.showMsgDialog(activity, "", getString(R.string.delete_all_confirm_msg), true, getString(R.string.cancel), null, getString(R.string.sure)) {
                //数据库删除所有条目
                DBManager.getItemsDAO()?.getAllItems()?.forEach {
                    DBManager.getItemsDAO()?.deleteItem(it)
                }
                PublicMsgEvent.dataChangeEvent(1) //通知数据更新
                CustomToast.showToast(getString(R.string.delete_success))
            }
        }
    }

    override fun initData() {
    }

}