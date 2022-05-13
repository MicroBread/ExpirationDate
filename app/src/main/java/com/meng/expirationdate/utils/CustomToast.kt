package com.meng.expirationdate.utils

import android.content.Context
import android.widget.Toast
import com.meng.expirationdate.base.BaseApplication

object CustomToast {
    fun showToast(text: CharSequence) {
        Toast.makeText(BaseApplication.instance(), text, Toast.LENGTH_SHORT).show()
    }

    fun showToast(context: Context, text: CharSequence) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(context: Context, text: CharSequence) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}