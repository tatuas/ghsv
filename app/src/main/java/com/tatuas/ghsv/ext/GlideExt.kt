package com.tatuas.ghsv.ext

import android.graphics.drawable.Drawable
import com.tatuas.ghsv.R
import com.tatuas.ghsv.data.glide.GlideRequest
import com.tatuas.ghsv.data.glide.GlideRequests

fun GlideRequests.loadGlass(nickname: String?): GlideRequest<Drawable> {
    val url = if (nickname.isNullOrEmpty()) ""
    else "https://grass-graph.moshimo.works/images/$nickname.png?background=none"

    return load(url)
}

fun GlideRequests.loadDefault(url: String?): GlideRequest<Drawable> {
    val load = if (url.isNullOrEmpty()) load("") else load(url)
    return load.placeholder(R.drawable.image_empty).error(R.drawable.image_empty)
}
