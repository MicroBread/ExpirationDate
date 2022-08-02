package com.meng.expirationdate.utils

import com.meng.expirationdate.R
import com.meng.expirationdate.base.BaseApplication
import com.meng.expirationdate.room.ItemType

object Utils {
    /**
     * dp转换为px
     * */
    @JvmStatic
    fun convertDpToPixelOfInt(dp: Int): Int {
        val resources = BaseApplication.instance().resources
        val metrics = resources.displayMetrics
        return (dp * metrics.density + 0.5f).toInt()
    }

    /**
     * 根据物品类型返回图片
     * */
    @JvmStatic
    fun getTypeDrawables(type: Int?): Int {
        return when (type ?: 0) {
            ItemType.CAN.type -> R.drawable.icon_can
            ItemType.MEAT.type -> R.drawable.icon_meat
            ItemType.VEGETABLE.type -> R.drawable.icon_vegetable
            ItemType.SEASONING.type -> R.drawable.icon_seaconing
            ItemType.SNACK.type -> R.drawable.icon_snack
            ItemType.DRINKS.type -> R.drawable.icon_drinks
            ItemType.FRUIT.type -> R.drawable.icon_fruit
            ItemType.COSMETIC.type -> R.drawable.icon_cosmetic
            else -> R.drawable.icon_obj_default
        }
    }

    /**
     * 根据物品类型名返回类型ID
     * */
    @JvmStatic
    fun getTypeByName(name: String?): Int {
        if (name.isNullOrBlank()) return -1
        return when (name) {
            ItemType.CAN.typeName -> ItemType.CAN.type
            ItemType.MEAT.typeName -> ItemType.MEAT.type
            ItemType.VEGETABLE.typeName -> ItemType.VEGETABLE.type
            ItemType.SEASONING.typeName -> ItemType.SEASONING.type
            ItemType.SNACK.typeName -> ItemType.SNACK.type
            ItemType.DRINKS.typeName -> ItemType.DRINKS.type
            ItemType.FRUIT.typeName -> ItemType.FRUIT.type
            ItemType.COSMETIC.typeName -> ItemType.COSMETIC.type
            else -> -1
        }
    }
}