package com.tatuas.ghsv.ext

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.tatuas.ghsv.data.viewmodel.ViewModelFactory

fun <T : ViewDataBinding> Fragment.buildDataBinding(@LayoutRes layoutId: Int,
                                                    inflater: LayoutInflater,
                                                    container: ViewGroup?): T =
        DataBindingUtil.inflate(inflater, layoutId, container, false)

fun <T : ViewModel> Fragment.buildViewModel(modelClass: Class<T>): T =
        ViewModelProviders.of(activity!!, ViewModelFactory(activity!!)).get(modelClass)
