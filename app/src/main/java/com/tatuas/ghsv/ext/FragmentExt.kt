package com.tatuas.ghsv.ext

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tatuas.ghsv.data.viewmodel.ViewModelFactory

fun <T : ViewDataBinding> Fragment.buildDataBinding(@LayoutRes layoutId: Int,
                                                    inflater: LayoutInflater,
                                                    container: ViewGroup?): T =
        DataBindingUtil.inflate(inflater, layoutId, container, false)

fun <T : ViewModel> Fragment.buildViewModel(modelClass: Class<T>): T =
        ViewModelProviders.of(activity!!, ViewModelFactory(activity!!)).get(modelClass)
