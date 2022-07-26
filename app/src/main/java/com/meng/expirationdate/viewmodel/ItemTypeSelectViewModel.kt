package com.meng.expirationdate.viewmodel

import com.meng.expirationdate.network.ObservableItemField

class ItemTypeSelectViewModel(bean: ItemTypeBean) {
    val type = ObservableItemField<Int>()
    val typeName = ObservableItemField<String>()
    val isSelected = ObservableItemField<Boolean>()

    init {
        type.set(bean.type)
        typeName.set(bean.typeName)
        isSelected.set(bean.select)
    }
}

data class ItemTypeBean(var type: Int, var typeName: String, var select: Boolean = false)