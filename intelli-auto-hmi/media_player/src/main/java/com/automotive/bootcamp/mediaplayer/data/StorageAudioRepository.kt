package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.cache.StorageMedia
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.AudioEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.PlaylistEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.AudioPlaylistCrossRef
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.PlaylistWithAudios

class StorageAudioRepository(private val cacheAudioSource: CacheAudioSource) : StorageMedia {
    override suspend fun insertAudio(audio: AudioEntity) {
        cacheAudioSource.insertAudio(audio)
    }

    override suspend fun insertAudios(audios: List<AudioEntity>) {
        cacheAudioSource.insertAudios(audios)
    }

    override suspend fun insertPlaylist(playlist: PlaylistEntity) {
        cacheAudioSource.insertPlaylist(playlist)
    }

    override suspend fun insertAudioPlaylistCrossRef(crossRef: AudioPlaylistCrossRef) {
        cacheAudioSource.insertAudioPlaylistCrossRef(crossRef)
    }

    override suspend fun deletePlaylist(pid: Int) {
        cacheAudioSource.deletePlaylist(pid)
    }

    override suspend fun deleteAudioFromPlaylist(aid: Int, pid: Int) {
        cacheAudioSource.deleteAudioFromPlaylist(aid, pid)
    }

    override suspend fun getPlaylistWithAudiosById(pid: Int): List<PlaylistWithAudios> {
        return cacheAudioSource.getPlaylistWithAudiosById(pid)
    }

    override suspend fun getPlaylistWithAudiosByType(type: String): List<PlaylistWithAudios> {
        return cacheAudioSource.getPlaylistsWithAudiosByType(type)
    }

    override suspend fun getAllPlaylistsWithAudios(): List<PlaylistWithAudios> {
        return cacheAudioSource.getAllPlaylistsWithAudios()
    }
}