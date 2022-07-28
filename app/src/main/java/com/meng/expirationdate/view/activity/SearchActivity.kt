package com.meng.expirationdate.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import com.blankj.utilcode.util.KeyboardUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.meng.expirationdate.R
import com.meng.expirationdate.base.BaseActivity
import com.meng.expirationdate.databinding.ActivitySearchBinding
import com.meng.expirationdate.event.PublicMsgEvent
import com.meng.expirationdate.room.DBManager
import com.meng.expirationdate.room.ItemInfo
import com.meng.expirationdate.utils.CustomToast
import com.meng.expirationdate.utils.Utils
import com.meng.expirationdate.utils.onClickNoAnim
import com.meng.expirationdate.view.adapter.ItemsAdapter
import com.meng.expirationdate.view.fragment.ItemDetailDialogFragment
import com.meng.expirationdate.viewmodel.SearchViewModel
import com.meng.expirationdate.widget.ActionSheet
import com.meng.expirationdate.widget.AlertMsgDialog
import com.meng.expirationdate.widget.WrapContentLinearLayoutManager

/**
 * 首页-主页-搜索
 * */
class SearchActivity: BaseActivity<ActivitySearchBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_search

    private val mViewModel by lazy {
        createVM<SearchViewModel>()
    }

    private lateinit var adapter: ItemsAdapter //列表适配器

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        mBinding.apply {
            vm = mViewModel
        }

        //获取焦点，弹出软键盘
        mBinding.etSearch.requestFocus()
        KeyboardUtils.showSoftInput()

        mBinding.tvCancel.onClickNoAnim {
            finish() //点击取消后返回首页
        }

        mBinding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //用户点击搜索
                if (mBinding.etSearch.text.toString().isBlank()) {
                    CustomToast.showToast(getString(R.string.search_input_null))
                } else {
                    val list = DBManager.getItemsDAO()?.searchItem(mBinding.etSearch.text.toString(), Utils.getTypeByName(mBinding.etSearch.text.toString()))
                    mViewModel.dataList.value?.clear()
                    if (list != null && list.isNotEmpty()) {
                        mViewModel.dataList.value?.addAll(list)
                    }
                    mViewModel.dataListSize.set(mViewModel.dataList.value?.size ?: 0)
                    adapter.notifyDataSetChanged()
                }
                KeyboardUtils.hideSoftInput(this@SearchActivity)
                true
            }
            false
        }

        adapter = ItemsAdapter(this, mViewModel.dataList.value!!)
        adapter.setOnItemClickListener(object : ItemsAdapter.OnItemClickListener {
            override fun itemClick(item: ItemInfo) {
                //查看详情弹窗
                val itemDetailDialogFragment = ItemDetailDialogFragment()
                val bundle = Bundle()
                bundle.putParcelable("itemInfo", item)
                itemDetailDialogFragment.arguments = bundle
                itemDetailDialogFragment.showNow(supportFragmentManager, "ItemDetailDialogFragment")
            }

            override fun itemLongClick(item: ItemInfo) {
                //长按条目
                val title = arrayOf(getString(R.string.edit), getString(R.string.delete))
                setTheme(R.style.ActionSheetStyleiOS)
                ActionSheet.createBuilder(supportFragmentManager, mBinding.clRoot)
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
                                    val intent = Intent(this@SearchActivity, AddItemActivity::class.java)
                                    val bundle = Bundle()
                                    bundle.putInt("type", 1)
                                    bundle.putParcelable("itemInfo", item)
                                    intent.putExtra("bundle", bundle)
                                    startActivity(intent)
                                }
                                1 -> {
                                    //删除条目
                                    AlertMsgDialog.showMsgDialog(this@SearchActivity, "", String.format(getString(R.string.delete_confirm_msg), item.itemName), true, getString(R.string.cancel), null, getString(R.string.sure)) {
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
                                            mViewModel.dataListSize.set(it.size)
                                        }
                                        PublicMsgEvent.dataChangeEvent(1)
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
        mBinding.rv.layoutManager = WrapContentLinearLayoutManager(this)
    }

    override fun initData() {
        LiveEventBus.get(PublicMsgEvent.DATA_CHANGE_EVENT, Int::class.java).observe(this) {
            //监听数据发生变化，刷新列表
            if (it == 0) {
                refreshAllData()
            }
        }
    }

    /**
     * 加载所有数据
     * */
    @SuppressLint("NotifyDataSetChanged")
    private fun refreshAllData() {
        val list = DBManager.getItemsDAO()?.searchItem(mBinding.etSearch.text.toString(), Utils.getTypeByName(mBinding.etSearch.text.toString()))
        mViewModel.dataList.value?.clear()
        if (list != null && list.isNotEmpty()) {
            mViewModel.dataList.value?.addAll(list)
        }
        mViewModel.dataListSize.set(mViewModel.dataList.value?.size ?: 0)
        adapter.notifyDataSetChanged()
    }
}