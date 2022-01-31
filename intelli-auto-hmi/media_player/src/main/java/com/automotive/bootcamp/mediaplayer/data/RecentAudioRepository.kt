package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.utils.RECENT_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.mediaplayer.data.models.PlaylistItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RecentAudioRepository(private val cacheAudioSource: CacheAudioSource) {
    private var pid:Long? = null

    init {
        runBlocking {
            launch(Dispatchers.IO)
            {
                pid = getEmbeddedPlaylist()?.id
            }
        }
    }

    suspend fun addAudio(aid: Long) {
        pid?.let {
            val crossRef = AudioPlaylistItemCrossRef(aid, it)
            cacheAudioSource.insertAudioPlaylistCrossRef(crossRef)
        }
    }

    suspend fun removeAudio(aid: Long) {
        pid?.let {
            val crossRef = AudioPlaylistItemCrossRef(aid, it)
            cacheAudioSource.deleteAudioFromPlaylist(crossRef)
        }
    }

    suspend fun hasAudio(aid: Long): Boolean {
        pid?.let {
            return cacheAudioSource.playlistHasAudio(it, aid)
        }
        return false
    }

    fun getPlaylist(): Flow<PlaylistItem?>? {
        pid?.let {
            return cacheAudioSource.getPlaylist(it)
        }
        return null
    }

    suspend fun getEmbeddedPlaylist():EmbeddedPlaylistItem? {
        return cacheAudioSource.getEmbeddedPlaylist(RECENT_PLAYLIST_NAME)
    }

    suspend fun addEmbeddedPlaylist(pid: Long) {
        val embeddedPlaylistItem = EmbeddedPlaylistItem(pid, RECENT_PLAYLIST_NAME)
        cacheAudioSource.insertEmbeddedPlaylist(embeddedPlaylistItem)
        this.pid = pid
    }
}