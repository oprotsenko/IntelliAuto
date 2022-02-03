package com.automotive.bootcamp.mediaplayer.service

import com.automotive.bootcamp.common.utils.Resource
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

interface ChildLoadedListener {
    fun onChildLoaded(audios: Resource<List<AudioWrapper>>)
}