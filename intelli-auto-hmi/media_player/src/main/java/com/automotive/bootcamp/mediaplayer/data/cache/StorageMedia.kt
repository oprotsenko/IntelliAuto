package com.automotive.bootcamp.mediaplayer.data.cache

import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.AudioEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.PlaylistEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.AudioPlaylistCrossRef
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.PlaylistWithAudios

interface StorageMedia {
    suspend fun insertAudio(audio: AudioEntity)
    suspend fun insertAudios(audios: List<AudioEntity>)
    suspend fun insertPlaylist(playlist: PlaylistEntity)
    suspend fun insertAudioPlaylistCrossRef(crossRef: AudioPlaylistCrossRef)
    suspend fun deletePlaylist(pid: Int)
    suspend fun deleteAudioFromPlaylist(aid: Int, pid: Int)
    suspend fun getPlaylistWithAudiosById(pid: Int): List<PlaylistWithAudios>
    suspend fun getPlaylistWithAudiosByType(type: String): List<PlaylistWithAudios>
    suspend fun getAllPlaylistsWithAudios(): List<PlaylistWithAudios>
}