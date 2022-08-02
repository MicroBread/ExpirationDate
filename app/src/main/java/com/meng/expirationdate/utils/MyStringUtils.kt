package com.meng.expirationdate.utils

import android.annotation.SuppressLint
import com.blankj.utilcode.util.StringUtils
import com.meng.expirationdate.R
import com.meng.expirationdate.room.ItemType
import java.text.SimpleDateFormat
import kotlin.math.abs

object MyStringUtils {
    /**
     * 根据时间戳获取日期
     * */
    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun getDate(date: Long?): String {
        if ((date ?: 0) > 0) {
            return try {
                SimpleDateFormat("yyyy-MM-dd").parse(date.toString())!!.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                StringUtils.getString(R.string.date_error)
            }

        }
        return StringUtils.getString(R.string.date_error)
    }

    /**
     * 根据时间戳获取日期
     * */
    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun getDateByTime(date: Long?): String {
        if ((date ?: 0) > 0) {
            return try {
                SimpleDateFormat("yyyy-MM-dd").format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                StringUtils.getString(R.string.date_error)
            }

        }
        return StringUtils.getString(R.string.date_error)
    }

    /**
     * 备注
     * */
    @JvmStatic
    fun getRemark(remark: String?): String {
        return if (remark.isNullOrBlank()) {
            StringUtils.getString(R.string.remark_null)
        } else {
            remark
        }
    }

    @JvmStatic
    fun formatDate(type: Int, dateStr: String): String {
        return String.format(StringUtils.getString(
            if (type == 0) R.string.production_date_format else R.string.expiration_date_format), dateStr)
    }

    /**
     * 根据类型ID获取类型名
     * */
    @JvmStatic
    fun getItemType(type: Int?): String {
        return when(type) {
            ItemType.CAN.type -> ItemType.CAN.typeName
            ItemType.MEAT.type -> ItemType.MEAT.typeName
            ItemType.VEGETABLE.type -> ItemType.VEGETABLE.typeName
            ItemType.SEASONING.type -> ItemType.SEASONING.typeName
            ItemType.SNACK.type -> ItemType.SNACK.typeName
            ItemType.DRINKS.type -> ItemType.DRINKS.typeName
            ItemType.FRUIT.type -> ItemType.FRUIT.typeName
            ItemType.COSMETIC.type -> ItemType.COSMETIC.typeName
            else -> ItemType.DEFAULT.typeName
        }
    }

    /**
     * 根据是否过期返回字符串
     * */
    @JvmStatic
    fun getTimeComparedResult(date: String?): String {
        if (date.isNullOrBlank()) return StringUtils.getString(R.string.date_error)
        return if (compareTime(date)) {
            StringUtils.getString(R.string.expired)
        } else {
            StringUtils.getString(R.string.not_expired)
        }
    }

    /**
     * 判断是否过期
     * */
    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun compareTime(date: String?): Boolean {
        val dateTime = if (date.isNullOrBlank()) {
            0L
        } else {
            SimpleDateFormat("yyyy-MM-dd").parse(date)?.time ?: 0L
        }
        val currentTime = System.currentTimeMillis()
        return (currentTime >= dateTime)
    }

    /**
     * 计算日期差值
     * */
    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun calculateTime(date: String?): String {
        val dateTime = if (date.isNullOrBlank()) {
            0L
        } else {
            SimpleDateFormat("yyyy-MM-dd").parse(date)?.time ?: 0L
        }
        val resultTime = abs(System.currentTimeMillis() - dateTime) / 1000 / 60 /60 / 24 //天数
        return String.format(StringUtils.getString(R.string.day_format), resultTime + 1)
    }
}