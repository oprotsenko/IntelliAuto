package com.automotive.bootcamp.mediaplayer.presentation.data

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadCircleImage(url: String) {
    Glide.with(this)
        .load(url)
        .into(this)
}