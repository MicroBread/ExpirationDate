package com.meng.expirationdate.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meng.expirationdate.network.ObservableItemField
import com.meng.expirationdate.network.PageStateType
import com.meng.expirationdate.network.RefreshType

/***
 * ViewModel的父类
 */

abstract class BaseViewModel : ViewModel() {
    protected val _operateActivity = MutableLiveData<Int>()

    val operateActivity : LiveData<Int>
        get() = _operateActivity

    //页面状态
    @PageStateType
    val pageState = ObservableItemField<Int>()
    //刷新/加载更多状态
    @RefreshType
    val listState = ObservableItemField<Int>()

    init {
        pageState.set(PageStateType.NORMAL)
        listState.set(RefreshType.NORMAL)
    }

//    override fun onCleared() {
//        super.onCleared()
//        Log.i("${javaClass.simpleName}:onCleared()")
//    }
}