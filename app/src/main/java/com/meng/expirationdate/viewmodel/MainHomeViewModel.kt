package com.meng.expirationdate.viewmodel

import androidx.lifecycle.MutableLiveData
import com.meng.expirationdate.base.BaseViewModel
import com.meng.expirationdate.room.ItemInfo

class MainHomeViewModel: BaseViewModel() {
    val dataList by lazy {
        MutableLiveData<MutableList<ItemInfo>>().also { it.value = ArrayList() }
    }
}