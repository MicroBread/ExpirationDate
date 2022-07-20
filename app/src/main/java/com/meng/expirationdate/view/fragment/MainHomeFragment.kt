package com.meng.expirationdate.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
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
import com.meng.expirationdate.view.adapter.ItemsAdapter
import com.meng.expirationdate.viewmodel.MainHomeViewModel
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
            startActivity(Intent(activity, AddItemActivity::class.java))
        }

        //初始化物品条目适配器
        adapter = ItemsAdapter(mContext, mViewModel.dataList.value!!)
        adapter.setOnItemClickListener(object : ItemsAdapter.OnItemClickListener {
            override fun itemClick(item: ItemInfo) {

            }

            override fun itemChildClick(view: View, item: ItemInfo, anim: AnimationDrawable) {
            }

        })
        mBinding.rv.adapter = adapter
        mBinding.rv.layoutManager = WrapContentLinearLayoutManager(mContext)
    }

    override fun initData() {
        refreshAllData()

        LiveEventBus.get(PublicMsgEvent.DATA_CHANGE_EVENT, Int::class.java).observe(this) {
            Log.e("test", "DATA_CHANGE_EVENT observe:$it")
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