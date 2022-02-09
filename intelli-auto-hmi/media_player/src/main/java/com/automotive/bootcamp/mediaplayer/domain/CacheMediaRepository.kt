package com.automotive.bootcamp.mediaplayer.domain

import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface CacheMediaRepository {
    suspend fun addAudios(audios: List<Audio>): List<Long>
    fun getAudios(pid: Long): Flow<Playlist?>
}