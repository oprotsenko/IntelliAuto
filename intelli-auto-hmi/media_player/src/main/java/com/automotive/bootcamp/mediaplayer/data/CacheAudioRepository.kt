package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.AudioEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.PlaylistEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.AudioPlaylistCrossRef
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.PlaylistWithAudios

class CacheAudioRepository(private val cacheAudioSource: CacheAudioSource) {
    suspend fun insertAudio(audio: AudioEntity) {
        cacheAudioSource.insertAudio(audio)
    }

    suspend fun insertAudios(audios: List<AudioEntity>) {
        cacheAudioSource.insertAudios(audios)
    }

    suspend fun insertPlaylist(playlist: PlaylistEntity) {
        cacheAudioSource.insertPlaylist(playlist)
    }

    suspend fun insertAudioPlaylistCrossRef(crossRef: AudioPlaylistCrossRef) {
        cacheAudioSource.insertAudioPlaylistCrossRef(crossRef)
    }

    suspend fun deletePlaylist(pid: Int) {
        cacheAudioSource.deletePlaylist(pid)
    }

    suspend fun deleteAudioFromPlaylist(aid: Int, pid: Int) {
        cacheAudioSource.deleteAudioFromPlaylist(aid, pid)
    }

    suspend fun getPlaylistWithAudiosById(pid: Int): List<PlaylistWithAudios> {
        return cacheAudioSource.getPlaylistWithAudiosById(pid)
    }

    suspend fun getPlaylistWithAudiosByType(type: String): List<PlaylistWithAudios> {
        return cacheAudioSource.getPlaylistsWithAudiosByType(type)
    }

    suspend fun getAllPlaylistsWithAudios(): List<PlaylistWithAudios> {
        return cacheAudioSource.getAllPlaylistsWithAudios()
    }
}