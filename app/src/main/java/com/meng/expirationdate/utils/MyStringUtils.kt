package com.meng.expirationdate.utils

import android.annotation.SuppressLint
import com.blankj.utilcode.util.StringUtils
import com.meng.expirationdate.R
import java.text.SimpleDateFormat

object MyStringUtils {
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
}