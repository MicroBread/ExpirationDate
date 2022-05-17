package com.meng.expirationdate.viewmodel

import com.meng.expirationdate.base.BaseViewModel
import com.meng.expirationdate.network.ObservableItemField

class AddItemViewModel: BaseViewModel() {
    var dateType = ObservableItemField<Int>() //过期计算方式 0保质天数 1过期日期
    var date = ObservableItemField<Long>()

    init {
        date.set(System.currentTimeMillis())
        dateType.set(0)
    }
}