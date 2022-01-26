package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.common.utils.FAVOURITE_PLAYLIST_NAME
import com.automotive.bootcamp.common.utils.RECENT_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.mediaplayer.data.models.PlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.extensions.mapToSpecialPlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.models.EmbeddedPlaylist

class FavouriteAudioRepository(private val cacheAudioSource: CacheAudioSource) {

    suspend fun addAudio(aid: Long) {
        val pid = cacheAudioSource.getEmbeddedPlaylist(FAVOURITE_PLAYLIST_NAME)?.id

        pid?.let {
            val crossRef = AudioPlaylistItemCrossRef(aid, it)
            cacheAudioSource.insertAudioPlaylistCrossRef(crossRef)
        }
    }

    suspend fun removeAudio(aid: Long) {
        val pid = cacheAudioSource.getEmbeddedPlaylist(FAVOURITE_PLAYLIST_NAME)?.id

        pid?.let {
            val crossRef = AudioPlaylistItemCrossRef(aid, it)
            cacheAudioSource.deleteAudioFromPlaylist(crossRef)
        }
    }

    suspend fun getPlaylist(): PlaylistItem? {
        val pid = cacheAudioSource.getEmbeddedPlaylist(FAVOURITE_PLAYLIST_NAME)?.id

        pid?.let {
            return cacheAudioSource.getPlaylist(it)
        }

        return null
    }

    suspend fun addEmbeddedPlaylist(pid: Long) {
        val embeddedPlaylistItem = EmbeddedPlaylistItem(pid, FAVOURITE_PLAYLIST_NAME)

        cacheAudioSource.insertEmbeddedPlaylist(embeddedPlaylistItem)
    }

    suspend fun getEmbeddedPlaylist(): EmbeddedPlaylistItem? {
        return cacheAudioSource.getEmbeddedPlaylist(FAVOURITE_PLAYLIST_NAME)
    }

    suspend fun isAudioExist(aid: Long) =
        cacheAudioSource.isAudioExist(aid)
}