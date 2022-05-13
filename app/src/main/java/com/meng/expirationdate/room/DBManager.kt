package com.meng.expirationdate.room

object DBManager {
    fun getItemsDAO(): ItemsDAO? {
        return CommonDatabase.instance.itemsDAO
    }
}