package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.utils.FAVOURITE_PLAYLIST_NAME
import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.mediaplayer.data.models.PlaylistItem

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

    suspend fun hasAudio(aid: Long): Boolean {
        val pid = cacheAudioSource.getEmbeddedPlaylist(FAVOURITE_PLAYLIST_NAME)?.id
        return if (pid != null) {
            cacheAudioSource.playlistHasAudio(pid, aid)
        } else false
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
}