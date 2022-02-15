package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.cache.CacheMediaSource
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToEmbeddedPlaylist
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToPlaylist
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.FavouriteMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.EmbeddedPlaylist
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.utils.FAVOURITE_PLAYLIST_NAME
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class FavouriteAudioRepository(
    private val cacheMediaSource: CacheMediaSource,
    private val dispatcher: CoroutineDispatcher
) : FavouriteMediaRepository {
    private var pid: Long? = null

    private val job = Job()
    private val repositoryScope = CoroutineScope(dispatcher + job)

    init {
        repositoryScope.launch {
            pid = getEmbeddedPlaylist()?.id
        }
    }

    override suspend fun addAudio(aid: Long): Unit = withContext(dispatcher) {
        pid?.let {
            val crossRef = AudioPlaylistItemCrossRef(aid, it)
            cacheMediaSource.insertAudioPlaylistCrossRef(crossRef)
        }
    }

    override suspend fun removeAudio(aid: Long): Unit = withContext(dispatcher) {
        pid?.let {
            val crossRef = AudioPlaylistItemCrossRef(aid, it)
            cacheMediaSource.deleteAudioFromPlaylist(crossRef)
        }
    }

    override suspend fun hasAudio(aid: Long): Boolean {
        return withContext(dispatcher) {
            pid?.let {
                cacheMediaSource.playlistHasAudio(it, aid)
            }
        } ?: false
    }

    override fun getPlaylist(): Flow<Playlist?> {
        return cacheMediaSource.getPlaylist(pid).flowOn(dispatcher).map { playlistItem ->
            playlistItem?.mapToPlaylist()
        }
    }

    override suspend fun getEmbeddedPlaylist(): EmbeddedPlaylist? =
        withContext(dispatcher) {
            cacheMediaSource.getEmbeddedPlaylist(FAVOURITE_PLAYLIST_NAME)?.mapToEmbeddedPlaylist()
        }

    override suspend fun addEmbeddedPlaylist(pid: Long) = withContext(dispatcher) {
        val embeddedPlaylistItem = EmbeddedPlaylistItem(pid, FAVOURITE_PLAYLIST_NAME)
        cacheMediaSource.insertEmbeddedPlaylist(embeddedPlaylistItem)
        this@FavouriteAudioRepository.pid = pid
    }
}