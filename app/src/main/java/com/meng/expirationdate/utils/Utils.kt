package com.meng.expirationdate.utils

import com.meng.expirationdate.R
import com.meng.expirationdate.base.BaseApplication
import com.meng.expirationdate.room.ItemType

object Utils {
    @JvmStatic
    fun convertDpToPixelOfInt(dp: Int): Int {
        val resources = BaseApplication.instance().resources
        val metrics = resources.displayMetrics
        return (dp * metrics.density + 0.5f).toInt()
    }

    @JvmStatic
    fun getTypeDrawables(type: Int?): Int {
        return when (type ?: 0) {
            ItemType.CAN.type -> R.drawable.icon_can
            ItemType.MEAT.type -> R.drawable.icon_meat
            ItemType.VEGETABLE.type -> R.drawable.icon_vegetable
            ItemType.SEASONING.type -> R.drawable.icon_seaconing
            ItemType.SNACK.type -> R.drawable.icon_snack
            ItemType.DRINKS.type -> R.drawable.icon_drinks
            ItemType.COSMETIC.type -> R.drawable.icon_cosmetic
            else -> R.drawable.icon_obj_default
        }
    }
}