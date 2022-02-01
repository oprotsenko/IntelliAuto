package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.utils.RECENT_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.mediaplayer.data.models.PlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.RecentMediaRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class RecentAudioRepository(
    private val cacheAudioSource: CacheAudioSource,
    private val dispatcher: CoroutineDispatcher
): RecentMediaRepository {
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

    override suspend fun getSize(): Int? =
        withContext(dispatcher) {
            pid?.let {
                cacheAudioSource.getPlaylistSize(it)
            }
            null
        }

    override suspend fun getEmbeddedPlaylist(): EmbeddedPlaylistItem? =
        withContext(dispatcher) {
            cacheAudioSource.getEmbeddedPlaylist(RECENT_PLAYLIST_NAME)
        }

    override suspend fun addEmbeddedPlaylist(pid: Long) = withContext(dispatcher) {
        val embeddedPlaylistItem = EmbeddedPlaylistItem(pid, RECENT_PLAYLIST_NAME)
        cacheAudioSource.insertEmbeddedPlaylist(embeddedPlaylistItem)
        this@RecentAudioRepository.pid = pid
    }
}