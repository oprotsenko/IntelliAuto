package com.automotive.bootcamp.mediaplayer.domain

import com.automotive.bootcamp.mediaplayer.data.models.AudioItem

interface CacheMediaRepository {
    suspend fun addAudios(audios: List<AudioItem>): List<Long>
    suspend fun addAudio(audio: AudioItem): Long
}