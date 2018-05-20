package com.tatuas.ghsv.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

fun <T> LiveData<T>.observeNullable(owner: LifecycleOwner, observer: (T?) -> Unit) {
    this.observe(owner, Observer {
        observer(it)
    })
}

fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, observer: (T) -> Unit) {
    this.observe(owner, Observer {
        if (it != null) {
            observer(it)
        }
    })
}
