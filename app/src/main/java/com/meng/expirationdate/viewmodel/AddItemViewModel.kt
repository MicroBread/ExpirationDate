package com.meng.expirationdate.viewmodel

import com.meng.expirationdate.base.BaseViewModel
import com.meng.expirationdate.network.ObservableItemField
import com.meng.expirationdate.room.ItemType

class AddItemViewModel: BaseViewModel() {
    var pageType = ObservableItemField<Int>() //页面类型 0添加 1编辑
    var dateType = ObservableItemField<Int>() //过期计算方式 0保质天数 1过期日期
    var itemType = ObservableItemField<Int>() //物品类型

    init {
        pageType.set(0)
        dateType.set(0)
        itemType.set(ItemType.DEFAULT.type)
    }
}