package com.meng.expirationdate.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus
import com.meng.expirationdate.R
import com.meng.expirationdate.base.BaseFragment
import com.meng.expirationdate.databinding.FragmentMainHomeBinding
import com.meng.expirationdate.event.PublicMsgEvent
import com.meng.expirationdate.room.DBManager
import com.meng.expirationdate.room.ItemInfo
import com.meng.expirationdate.utils.CustomToast
import com.meng.expirationdate.utils.onClickNoAnim
import com.meng.expirationdate.view.activity.AddItemActivity
import com.meng.expirationdate.view.activity.MainActivity
import com.meng.expirationdate.view.adapter.ItemsAdapter
import com.meng.expirationdate.viewmodel.MainHomeViewModel
import com.meng.expirationdate.widget.ActionSheet
import com.meng.expirationdate.widget.AlertMsgDialog
import com.meng.expirationdate.widget.WrapContentLinearLayoutManager

class MainHomeFragment: BaseFragment<FragmentMainHomeBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_main_home

    private val mViewModel by lazy {
        createVM<MainHomeViewModel>()
    }
    private lateinit var adapter: ItemsAdapter

    override fun initView() {
        mBinding.apply {
            vm = mViewModel
        }

        mBinding.imageSearch.onClickNoAnim {
            //todo 搜索页面
            CustomToast.showToast("搜索页待完成")
        }

        mBinding.imageAdd.onClickNoAnim {
            //跳转添加页面
            val intent = Intent(activity, AddItemActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("type", 0)
            intent.putExtra("bundle", bundle)
            startActivity(intent)
        }

        //初始化物品条目适配器
        adapter = ItemsAdapter(mContext, mViewModel.dataList.value!!)
        adapter.setOnItemClickListener(object : ItemsAdapter.OnItemClickListener {
            override fun itemClick(item: ItemInfo) {
                //查看详情弹窗
                activity?.supportFragmentManager?.let {
                    val itemDetailDialogFragment = ItemDetailDialogFragment()
                    val bundle = Bundle()
                    bundle.putParcelable("itemInfo", item)
                    itemDetailDialogFragment.arguments = bundle
                    itemDetailDialogFragment.showNow(it, "ItemDetailDialogFragment")
                }
            }

            override fun itemLongClick(item: ItemInfo) {
                //长按条目
                val title = arrayOf(getString(R.string.edit), getString(R.string.delete))
                activity?.setTheme(R.style.ActionSheetStyleiOS)
                ActionSheet.createBuilder(activity?.supportFragmentManager, (activity as MainActivity).getRootView())
                    .setCancelButtonTitle(getString(R.string.cancel))
                    .setOtherButtonTitles(title)
                    .setHasPromit(false)
                    .setSpecial(getString(R.string.delete))
                    .setCancelableOnTouchOutside(true)
                    .setListener(object : ActionSheet.ActionSheetListener {
                        override fun onDismiss(actionSheet: ActionSheet, isCancel: Boolean) {}
                        override fun onOtherButtonClick(actionSheet: ActionSheet, index: Int) {
                            actionSheet.dismiss()
                            when (index) {
                                0 -> {
                                    //编辑条目
                                    val intent = Intent(activity, AddItemActivity::class.java)
                                    val bundle = Bundle()
                                    bundle.putInt("type", 1)
                                    bundle.putParcelable("itemInfo", item)
                                    intent.putExtra("bundle", bundle)
                                    startActivity(intent)
                                }
                                1 -> {
                                    //删除条目
                                    AlertMsgDialog.showMsgDialog(activity, "", String.format(getString(R.string.delete_confirm_msg), item.itemName), true, getString(R.string.cancel), null, getString(R.string.sure)) {
                                        //数据库删除
                                        DBManager.getItemsDAO()?.deleteItem(item)
                                        //显示列表删除
                                        mViewModel.dataList.value?.let {
                                            for (listIndex in 0 until it.size) {
                                                if (item.itemId == it[listIndex].itemId) {
                                                    it.removeAt(listIndex)
                                                    adapter.notifyItemRemoved(listIndex)
                                                    CustomToast.showToast(getString(R.string.delete_success))
                                                    break
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }).show()
            }

            override fun itemChildClick(view: View, item: ItemInfo, anim: AnimationDrawable) {
            }

        })
        mBinding.rv.adapter = adapter
        mBinding.rv.layoutManager = WrapContentLinearLayoutManager(mContext)
    }

    override fun initData() {
        refreshAllData() //加载所有item数据

        LiveEventBus.get(PublicMsgEvent.DATA_CHANGE_EVENT, Int::class.java).observe(this) {
            //监听数据发生变化，刷新列表
            refreshAllData()
        }
    }

    /**
     * 加载所有数据
     * */
    @SuppressLint("NotifyDataSetChanged")
    private fun refreshAllData() {
        val list = DBManager.getItemsDAO()?.getAllItems()
        if (list != null && list.isNotEmpty()) {
            mViewModel.dataList.value?.clear()
            mViewModel.dataList.value?.addAll(list)
            adapter.notifyDataSetChanged()
        }
    }
}