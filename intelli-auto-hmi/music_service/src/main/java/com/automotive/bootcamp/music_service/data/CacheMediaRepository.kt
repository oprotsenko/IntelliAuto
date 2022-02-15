package com.automotive.bootcamp.music_service.data

import com.automotive.bootcamp.music_service.data.models.AudioItem
import com.automotive.bootcamp.music_service.data.models.PlaylistItem
import kotlinx.coroutines.flow.Flow

interface CacheMediaRepository {
    fun getAllPlaylists(): Flow<List<PlaylistItem>?>
    suspend fun insertAudios(audios: List<AudioItem>): List<Long>
    suspend fun addToRecent(aid: Long)
    suspend fun getRecentAudios(): List<AudioItem>?
}