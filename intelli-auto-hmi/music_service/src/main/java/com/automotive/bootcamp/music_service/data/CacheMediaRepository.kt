package com.automotive.bootcamp.music_service.data

import com.automotive.bootcamp.music_service.data.models.AudioItem


interface CacheMediaRepository {
    suspend fun addAudios(audios: List<AudioItem>): List<Long>
    suspend fun addAudio(audio: AudioItem): Long
}