package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.common.utils.FAVOURITE_PLAYLIST_ID
import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.data.models.PlaylistItem

class FavouriteAudioRepository(private val cacheAudioSource: CacheAudioSource) {
//    suspend fun createPlaylist() {
//        if (!cacheAudioSource.playlistExists(FAVOURITE_PLAYLIST_ID)) {
//            val recentPlaylist = PlaylistItem(FAVOURITE_PLAYLIST_ID, FAVOURITE_PLAYLIST_NAME, null)
//
//            cacheAudioSource.insertPlaylist(recentPlaylist)
//        }
//    }

    suspend fun addAudio(aid: Long) {
        val crossRef = AudioPlaylistItemCrossRef(aid, FAVOURITE_PLAYLIST_ID)
        cacheAudioSource.insertAudioPlaylistCrossRef(crossRef)
    }

    suspend fun removeAudio(aid: Long) {
        val crossRef = AudioPlaylistItemCrossRef(aid, FAVOURITE_PLAYLIST_ID)

        cacheAudioSource.deleteAudioFromPlaylist(crossRef)
    }

    suspend fun getPlaylist(): PlaylistItem? {
        return cacheAudioSource.getPlaylist(FAVOURITE_PLAYLIST_ID)
    }

    suspend fun playlistExists(): Boolean {
        return cacheAudioSource.playlistExists(FAVOURITE_PLAYLIST_ID)
    }
}