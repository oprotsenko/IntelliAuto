package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.cache.CacheMediaSource
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToEmbeddedPlaylist
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToPlaylist
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.RecentMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.EmbeddedPlaylist
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import com.automotive.bootcamp.mediaplayer.utils.RECENT_PLAYLIST_NAME
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class RecentAudioRepository(
    private val cacheMediaSource: CacheMediaSource,
    private val dispatcher: CoroutineDispatcher
) : RecentMediaRepository {
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

    override fun getPlaylist(): Flow<Playlist?>? {
        pid?.let {
            return cacheMediaSource.getPlaylist(it).flowOn(dispatcher)?.map { playlistItem ->
                playlistItem?.mapToPlaylist()
            }
        }
        return null
    }

    override suspend fun getSize(): Int? =
        withContext(dispatcher) {
            pid?.let {
                cacheMediaSource.getPlaylistSize(it)
            }
            null
        }

    override suspend fun getEmbeddedPlaylist(): EmbeddedPlaylist? =
        withContext(dispatcher) {
            cacheMediaSource.getEmbeddedPlaylist(RECENT_PLAYLIST_NAME)?.mapToEmbeddedPlaylist()
        }

    override suspend fun addEmbeddedPlaylist(pid: Long) = withContext(dispatcher) {
        val embeddedPlaylistItem = EmbeddedPlaylistItem(pid, RECENT_PLAYLIST_NAME)
        cacheMediaSource.insertEmbeddedPlaylist(embeddedPlaylistItem)
        this@RecentAudioRepository.pid = pid
    }
}