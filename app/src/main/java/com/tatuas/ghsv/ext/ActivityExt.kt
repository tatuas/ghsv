package com.tatuas.ghsv.ext

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import com.tatuas.ghsv.data.viewmodel.ViewModelFactory

fun <T : ViewModel> AppCompatActivity.buildViewModel(modelClass: Class<T>): Lazy<T> = lazy {
    ViewModelProviders.of(this, ViewModelFactory(applicationContext)).get(modelClass)
}
