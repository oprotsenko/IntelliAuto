package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.CacheAudioRepository
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.AudioEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.PlaylistEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.AudioPlaylistCrossRef
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.PlaylistWithAudios
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository

class GetLocalMusic(private val repositoryLocal: LocalMediaRepository, private val repositoryCache: CacheAudioRepository) {
    suspend fun getLocalSongs() = repositoryLocal.getLocalSongs()

    suspend fun insertAudio(audio: AudioEntity) {
        repositoryCache.insertAudio(audio)
    }

    suspend fun insertAudios(audios: List<AudioEntity>) {
        repositoryCache.insertAudios(audios)
    }

    suspend fun insertPlaylist(playlist: PlaylistEntity) {
        repositoryCache.insertPlaylist(playlist)
    }

    suspend fun insertAudioPlaylistCrossRef(crossRef: AudioPlaylistCrossRef) {
        repositoryCache.insertAudioPlaylistCrossRef(crossRef)
    }

    suspend fun deletePlaylist(pid: Int) {
        repositoryCache.deletePlaylist(pid)
    }

    suspend fun deleteAudioFromPlaylist(aid: Int, pid: Int) {
        repositoryCache.deleteAudioFromPlaylist(aid, pid)
    }

    suspend fun getPlaylistWithAudiosById(pid: Int): List<PlaylistWithAudios> {
        return repositoryCache.getPlaylistWithAudiosById(pid)
    }

    suspend fun getPlaylistsWithAudiosByType(type: String): List<PlaylistWithAudios> {
        return repositoryCache.getPlaylistWithAudiosByType(type)
    }

    suspend fun getAllPlaylistsWithAudios(): List<PlaylistWithAudios> {
        return repositoryCache.getAllPlaylistsWithAudios()
    }
}