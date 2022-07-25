package com.meng.expirationdate.utils

import com.meng.expirationdate.base.BaseApplication

object Utils {
    @JvmStatic
    fun convertDpToPixelOfInt(dp: Int): Int {
        val resources = BaseApplication.instance().resources
        val metrics = resources.displayMetrics
        return (dp * metrics.density + 0.5f).toInt()
    }
}