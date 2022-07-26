package com.meng.expirationdate.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class ItemInfo(
    @PrimaryKey val itemId: Long, //物品ID
    var itemName: String, //物品名称
    var itemNum: Int, //物品数量
    var itemDescription: String?, //物品备注
    var itemType: Int?, //物品类型
    var itemProductionDate: String?, //生产日期
    var itemExpirationDate: String?, //过期日期
): Parcelable {
    override fun toString(): String {
        return "物品ID:$itemId, 物品名称:$itemName, 物品数量:$itemNum, 物品备注:$itemDescription, 物品类型:$itemType, 生产日期:$itemProductionDate, 过期日期:$itemExpirationDate"
    }
}

/**
    物品类型枚举
 */
enum class ItemType(val type: Int, val typeName: String) {
    DEFAULT(0, "默认"),
    CAN(101, "罐头"),
    MEAT(102, "肉类"),
    VEGETABLE(103, "蔬菜"),
    SEASONING(104, "调味料"),
    SNACK(105, "零食"),
    DRINKS(106, "饮料"),
    COSMETIC(201, "化妆品"),
}