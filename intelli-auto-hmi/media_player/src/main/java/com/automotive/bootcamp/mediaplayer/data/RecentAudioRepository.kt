package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.common.utils.RECENT_PLAYLIST_ID
import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.data.models.PlaylistItem

class RecentAudioRepository(private val cacheAudioSource: CacheAudioSource) {
//    suspend fun createPlaylist() {
//        if (!cacheAudioSource.playlistExists(RECENT_PLAYLIST_ID)) {
//            val recentPlaylist = PlaylistItem(RECENT_PLAYLIST_ID, RECENT_PLAYLIST_NAME, null)
//
//            cacheAudioSource.insertPlaylist(recentPlaylist)
//        }
//    }

    suspend fun addAudio(audio: AudioItem) {
        cacheAudioSource.insertAudio(audio)

        val crossRef = AudioPlaylistItemCrossRef(audio.id, RECENT_PLAYLIST_ID)
        cacheAudioSource.insertAudioPlaylistCrossRef(crossRef)
    }

    suspend fun removeAudio(aid: Long) {
        val crossRef = AudioPlaylistItemCrossRef(aid, RECENT_PLAYLIST_ID)

        cacheAudioSource.deleteAudioFromPlaylist(crossRef)
    }

    suspend fun getPlaylist(): PlaylistItem? {
        return cacheAudioSource.getPlaylist(RECENT_PLAYLIST_ID)
    }

    suspend fun exists(): Boolean {
        return cacheAudioSource.playlistExists(RECENT_PLAYLIST_ID)
    }
}