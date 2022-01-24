package com.automotive.bootcamp.mediaplayer.domain.useCases

import com.automotive.bootcamp.mediaplayer.data.StorageAudioRepository
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.AudioEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.PlaylistEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.AudioPlaylistCrossRef
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.PlaylistWithAudios
import com.automotive.bootcamp.mediaplayer.domain.LocalMediaRepository

class GetLocalMusic(private val repositoryLocal: LocalMediaRepository, private val repositoryStorage: StorageAudioRepository) {
    suspend fun getLocalSongs() = repositoryLocal.retrieveLocalAudio()

    suspend fun insertAudio(audio: AudioEntity) {
        repositoryStorage.insertAudio(audio)
    }

    suspend fun insertAudios(audios: List<AudioEntity>) {
        repositoryStorage.insertAudios(audios)
    }

    suspend fun insertPlaylist(playlist: PlaylistEntity) {
        repositoryStorage.insertPlaylist(playlist)
    }

    suspend fun insertAudioPlaylistCrossRef(crossRef: AudioPlaylistCrossRef) {
        repositoryStorage.insertAudioPlaylistCrossRef(crossRef)
    }

    suspend fun deletePlaylist(pid: Int) {
        repositoryStorage.deletePlaylist(pid)
    }

    suspend fun deleteAudioFromPlaylist(aid: Int, pid: Int) {
        repositoryStorage.deleteAudioFromPlaylist(aid, pid)
    }

    suspend fun getPlaylistWithAudiosById(pid: Int): List<PlaylistWithAudios> {
        return repositoryStorage.getPlaylistWithAudiosById(pid)
    }

    suspend fun getPlaylistsWithAudiosByType(type: String): List<PlaylistWithAudios> {
        return repositoryStorage.getPlaylistWithAudiosByType(type)
    }

    suspend fun getAllPlaylistsWithAudios(): List<PlaylistWithAudios> {
        return repositoryStorage.getAllPlaylistsWithAudios()
    }
}