package com.meng.expirationdate.utils

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.guoyang.recyclerviewbindingadapter.ItemClickPresenter

@BindingAdapter("onClickNoAnim")
fun onClickNoAnim(view: View,call:((View)->Unit)?=null){
    call?.let {
        view.onClickNoAnim(call = it)
    }
}

@BindingAdapter(value = ["drawableRes"], requireAll = false)
fun setDrawableRes(imageView: ImageView?, drawableRes: Int) {
    imageView?.setImageResource(drawableRes)
}

@BindingAdapter("itemClickPresenter","itemPosition","itemBean",requireAll = true)
fun <T> onItemClick(view: View, call: ItemClickPresenter<T>?=null, position:Int?=null, bean:T?=null){
    if(call!=null && position!=null && bean!=null){
        view.onClickNoAnim {
            call.onItemClick(it,position,bean)
        }
    }
}