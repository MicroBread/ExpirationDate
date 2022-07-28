package com.meng.expirationdate.viewmodel

import com.meng.expirationdate.base.BaseViewModel
import com.meng.expirationdate.network.ObservableItemField

class MainSettingViewModel: BaseViewModel() {
    val versionName = ObservableItemField<String>() //版本号
}