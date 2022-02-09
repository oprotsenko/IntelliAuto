package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.cache.CacheMediaSource
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToAudioItem
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToPlaylist
import com.automotive.bootcamp.mediaplayer.domain.CacheMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.Audio
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CacheAudioRepository(
    private val cacheMediaSource: CacheMediaSource,
    private val dispatcher: CoroutineDispatcher
) : CacheMediaRepository {

    override suspend fun addAudios(audios: List<Audio>): List<Long> =
        withContext(dispatcher) {
            cacheMediaSource.insertAudios(audios.map {
                it.mapToAudioItem()
            })
        }

    override fun getAudios(pid: Long): Flow<Playlist?> =
        cacheMediaSource.getPlaylist(pid).map { playlistItem ->
            playlistItem?.mapToPlaylist()
        }
}