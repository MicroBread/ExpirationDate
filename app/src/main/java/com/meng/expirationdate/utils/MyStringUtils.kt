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
                SimpleDateFormat("yyyy年MM月dd日").parse(date.toString())!!.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                StringUtils.getString(R.string.date_error)
            }

        }
        return StringUtils.getString(R.string.date_error)
    }
}