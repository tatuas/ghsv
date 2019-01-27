package com.tatuas.ghsv.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

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
