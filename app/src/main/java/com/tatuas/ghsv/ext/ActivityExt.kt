package com.tatuas.ghsv.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.tatuas.ghsv.data.viewmodel.ViewModelFactory

fun <T : ViewModel> AppCompatActivity.buildViewModel(modelClass: Class<T>): Lazy<T> = lazy {
    ViewModelProviders.of(this, ViewModelFactory(applicationContext)).get(modelClass)
}
