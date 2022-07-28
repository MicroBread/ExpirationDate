package com.meng.expirationdate.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.guoyang.recyclerviewbindingadapter.ItemClickPresenter
import com.guoyang.recyclerviewbindingadapter.adapter.SingleTypeAdapter
import com.guoyang.recyclerviewbindingadapter.observable.ObservableAdapterList
import com.meng.expirationdate.R
import com.meng.expirationdate.base.BaseDialogFragment
import com.meng.expirationdate.databinding.FragmentDialogItemTypeBinding
import com.meng.expirationdate.room.ItemType
import com.meng.expirationdate.utils.Utils
import com.meng.expirationdate.view.activity.AddItemActivity
import com.meng.expirationdate.viewmodel.ItemTypeBean
import com.meng.expirationdate.viewmodel.ItemTypeSelectViewModel
import com.meng.expirationdate.widget.CommonItemDecoration

class ItemTypeDialogFragment: BaseDialogFragment<FragmentDialogItemTypeBinding>(), ItemClickPresenter<ItemTypeSelectViewModel> {
    override fun getLayoutId(): Int = R.layout.fragment_dialog_item_type

    private val mAdapter by lazy {
        context?.let {
            SingleTypeAdapter(it, R.layout.item_type_select, observableList)
                .apply { this.itemPresenter = this@ItemTypeDialogFragment }
        }
    }

    private var itemTypeSelected: Int = ItemType.DEFAULT.type //已选择的物品类型
    private var observableList = ObservableAdapterList<ItemTypeSelectViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Bottom_Translucent_NoTitle_hasDimEnabled)
    }

    override fun onStart() {
        super.onStart()
        val layoutParams = dialog?.window?.attributes
        layoutParams?.gravity = Gravity.NO_GRAVITY
        layoutParams?.height = Utils.convertDpToPixelOfInt(370)
        layoutParams?.width = Utils.convertDpToPixelOfInt(280)
        layoutParams?.dimAmount = 0.8f
        dialog?.window?.attributes = layoutParams
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.setCancelable(true)
    }

    override fun initView() {
        itemTypeSelected = arguments?.getInt("itemType") ?: ItemType.DEFAULT.type //由上层传入的物品类型

        mBinding.apply {
            recyclerView.layoutManager = GridLayoutManager(context, 3)
            context?.run {
                recyclerView.addItemDecoration(CommonItemDecoration(Utils.convertDpToPixelOfInt(10), 0))
            }
            recyclerView.adapter = mAdapter
        }
    }

    override fun initData() {
        //添加所有类型
        val array = mutableListOf<ItemTypeSelectViewModel>()
        array.add(ItemTypeSelectViewModel(ItemTypeBean(ItemType.DEFAULT.type, ItemType.DEFAULT.typeName, itemTypeSelected == ItemType.DEFAULT.type)))
        array.add(ItemTypeSelectViewModel(ItemTypeBean(ItemType.CAN.type, ItemType.CAN.typeName, itemTypeSelected == ItemType.CAN.type)))
        array.add(ItemTypeSelectViewModel(ItemTypeBean(ItemType.MEAT.type, ItemType.MEAT.typeName, itemTypeSelected == ItemType.MEAT.type)))
        array.add(ItemTypeSelectViewModel(ItemTypeBean(ItemType.VEGETABLE.type, ItemType.VEGETABLE.typeName, itemTypeSelected == ItemType.VEGETABLE.type)))
        array.add(ItemTypeSelectViewModel(ItemTypeBean(ItemType.SEASONING.type, ItemType.SEASONING.typeName, itemTypeSelected == ItemType.SEASONING.type)))
        array.add(ItemTypeSelectViewModel(ItemTypeBean(ItemType.SNACK.type, ItemType.SNACK.typeName, itemTypeSelected == ItemType.SNACK.type)))
        array.add(ItemTypeSelectViewModel(ItemTypeBean(ItemType.DRINKS.type, ItemType.DRINKS.typeName, itemTypeSelected == ItemType.DRINKS.type)))
        array.add(ItemTypeSelectViewModel(ItemTypeBean(ItemType.FRUIT.type, ItemType.FRUIT.typeName, itemTypeSelected == ItemType.FRUIT.type)))
        array.add(ItemTypeSelectViewModel(ItemTypeBean(ItemType.COSMETIC.type, ItemType.COSMETIC.typeName, itemTypeSelected == ItemType.COSMETIC.type)))
        observableList.setNewData(array)
    }

    override fun onItemClick(v: View, position: Int, item: ItemTypeSelectViewModel) {
        //选择类型点击事件
        val list = mutableListOf<ItemTypeSelectViewModel>()
        observableList.forEachIndexed { index, itemTypeSelectViewModel ->
            if (index != position) {
                itemTypeSelectViewModel.isSelected.set(false)
            } else {
                itemTypeSelected = item.type.get() ?: ItemType.DEFAULT.type
                itemTypeSelectViewModel.isSelected.set(true)
            }
            list.add(itemTypeSelectViewModel)
        }
        observableList.setNewData(list)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as? AddItemActivity)?.setItemType(itemTypeSelected) //返回选择的类型到上层
    }
}