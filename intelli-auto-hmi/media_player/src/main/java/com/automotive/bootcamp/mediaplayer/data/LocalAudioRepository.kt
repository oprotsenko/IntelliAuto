package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.localRepository.LocalMedia
import com.automotive.bootcamp.mediaplayer.data.localRepository.models.AudioItem
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository

class LocalAudioRepository(private val localMedia: LocalMedia):
    LocalMediaRepository {

    override suspend fun retrieveLocalAudio(): List<AudioItem> {
       return localMedia.retrieveLocalAudio()
    }
}