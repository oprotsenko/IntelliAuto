package com.automotive.bootcamp.music_service.data

import com.automotive.bootcamp.music_service.data.cache.CacheAudioSource
import com.automotive.bootcamp.music_service.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.music_service.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.music_service.data.models.PlaylistItem
import com.automotive.bootcamp.music_service.data.utils.FAVOURITE_PLAYLIST_NAME
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class FavouriteAudioRepository(
    private val cacheAudioSource: CacheAudioSource,
    private val dispatcher: CoroutineDispatcher
) : FavouriteMediaRepository{
    private var pid: Long? = null

    init {
        runBlocking {
            launch(dispatcher)
            {
                pid = getEmbeddedPlaylist()?.id
            }
        }
    }

    override suspend fun addAudio(aid: Long): Unit = withContext(dispatcher) {
        pid?.let {
            val crossRef = AudioPlaylistItemCrossRef(aid, it)
            cacheAudioSource.insertAudioPlaylistCrossRef(crossRef)
        }
    }

    override suspend fun removeAudio(aid: Long): Unit = withContext(dispatcher) {
        pid?.let {
            val crossRef = AudioPlaylistItemCrossRef(aid, it)
            cacheAudioSource.deleteAudioFromPlaylist(crossRef)
        }
    }

    override suspend fun hasAudio(aid: Long): Boolean =
        withContext(dispatcher) {
            pid?.let {
                cacheAudioSource.playlistHasAudio(it, aid)
            }
            false
        }

    override fun getPlaylist(): Flow<PlaylistItem?>? {
        pid?.let {
            return cacheAudioSource.getPlaylist(it).flowOn(dispatcher)
        }
        return null
    }

    override suspend fun getEmbeddedPlaylist(): EmbeddedPlaylistItem? =
        withContext(dispatcher) {
            cacheAudioSource.getEmbeddedPlaylist(FAVOURITE_PLAYLIST_NAME)
        }

    override suspend fun addEmbeddedPlaylist(pid: Long) = withContext(dispatcher) {
        val embeddedPlaylistItem = EmbeddedPlaylistItem(pid, FAVOURITE_PLAYLIST_NAME)
        cacheAudioSource.insertEmbeddedPlaylist(embeddedPlaylistItem)
        this@FavouriteAudioRepository.pid = pid
    }
}