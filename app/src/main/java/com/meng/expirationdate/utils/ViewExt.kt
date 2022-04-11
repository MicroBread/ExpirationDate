package com.meng.expirationdate.utils

import android.view.View
import com.blankj.utilcode.util.ClickUtils

fun View.onClickNoAnim(duration: Long = 500, call: (View)->Unit){
    setOnClickListener(object: ClickUtils.OnDebouncingClickListener(true, duration){
        override fun onDebouncingClick(v: View?) {
            call(this@onClickNoAnim)
        }
    })
}