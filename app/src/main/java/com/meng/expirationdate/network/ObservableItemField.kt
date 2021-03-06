package com.meng.expirationdate.network

import androidx.lifecycle.MutableLiveData

class ObservableItemField<T> : MutableLiveData<T>() {

    fun set(value: T) {
        setValue(value)
    }

    fun get(): T? {
        return super.getValue()
    }
}