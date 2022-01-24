package com.automotive.bootcamp.mediaplayer.utils.extensions

fun Int.timeToString(): String{
    val minutes = this / 60
    val seconds = this % 60

    return String.format("%d:%02d", minutes, seconds)
}