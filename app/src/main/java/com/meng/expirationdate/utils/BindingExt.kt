package com.meng.expirationdate.utils

import android.view.View

@androidx.databinding.BindingAdapter("app:onClickNoAnim")
fun onClickNoAnim(view: View,call:((View)->Unit)?=null){
    call?.let {
        view.onClickNoAnim(call = it)
    }
}