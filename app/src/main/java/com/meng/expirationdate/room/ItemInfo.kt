package com.meng.expirationdate.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ItemInfo(
    @PrimaryKey val itemId: Long, //物品ID
    var itemName: String, //物品名称
    var itemDescription: String?, //物品备注
    var itemType: Int?, //物品类型
    var itemProductionDate: Long?, //生产日期
    var itemExpirationDate: Long?, //保质期
)

/**
    物品类型枚举
 */
enum class ItemType(val type: Int) {
    DEFAULT(0), //默认
    CAN(101), //罐头
    MEAT(102), //肉类
    VEGETABLE(103), //蔬菜
    SEASONING(104), //调味料
    LIPSTICK(201), //口红
}