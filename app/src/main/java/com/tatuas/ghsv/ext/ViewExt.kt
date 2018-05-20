package com.tatuas.ghsv.ext

import android.view.View

fun View.toGone() {
    if (visibility != View.GONE) visibility = View.GONE
}

fun View.toVisible() {
    if (visibility != View.VISIBLE) visibility = View.VISIBLE
}
