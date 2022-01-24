package com.automotive.bootcamp.mediaplayer.data.storage.room

import android.content.Context
import com.automotive.bootcamp.mediaplayer.data.storage.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.storage.room.entities.AudioEntity
import com.automotive.bootcamp.mediaplayer.data.storage.room.entities.PlaylistEntity
import com.automotive.bootcamp.mediaplayer.data.storage.room.entities.relations.AudioPlaylistCrossRef
import com.automotive.bootcamp.mediaplayer.data.storage.room.entities.relations.PlaylistWithAudios

class RoomAudioSource(context: Context): CacheAudioSource {
    private val dao = RoomAudioDatabase.getInstance(context).audioDao
    override suspend fun insertAudio(audio: AudioEntity) {
        dao.insertAudio(audio)
    }

    override suspend fun insertAudios(audios: List<AudioEntity>) {
        dao.insertAudios(audios)
    }

    override suspend fun insertPlaylist(playlist: PlaylistEntity) {
        dao.insertPlaylist(playlist)
    }

    override suspend fun insertAudioPlaylistCrossRef(crossRef: AudioPlaylistCrossRef) {
        dao.insertAudioPlaylistCrossRef(crossRef)
    }

    override suspend fun deletePlaylist(pid: Int) {
        dao.deletePlaylist(pid)
    }

    override suspend fun deleteAudioFromPlaylist(aid: Int, pid: Int) {
        dao.deleteAudioFromPlaylist(aid, pid)
    }

    override suspend fun getPlaylistWithAudiosById(pid: Int): List<PlaylistWithAudios> {
        return dao.getPlaylistWithAudiosById(pid)
    }

    override suspend fun getPlaylistsWithAudiosByType(type: String): List<PlaylistWithAudios> {
        return dao.getPlaylistsWithAudiosByType(type)
    }

    override suspend fun getAllPlaylistsWithAudios(): List<PlaylistWithAudios> {
        return dao.getAllPlaylistsWithAudios()
    }
}