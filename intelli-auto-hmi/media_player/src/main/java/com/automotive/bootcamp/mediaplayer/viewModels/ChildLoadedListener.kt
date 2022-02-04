package com.automotive.bootcamp.mediaplayer.viewModels

import com.automotive.bootcamp.common.utils.Resource
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

interface ChildLoadedListener {
    fun onChildLoaded(audios: Resource<List<AudioWrapper>>)
}