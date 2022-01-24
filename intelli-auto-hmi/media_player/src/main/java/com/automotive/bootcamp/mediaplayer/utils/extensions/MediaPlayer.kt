package com.automotive.bootcamp.mediaplayer.utils.extensions

import android.media.MediaPlayer

val MediaPlayer.seconds: Int
    get() {
        return this.duration / 1000
    }

val MediaPlayer.currentSeconds: Int
    get() {
        return this.currentPosition / 1000
    }