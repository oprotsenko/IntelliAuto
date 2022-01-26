package com.automotive.bootcamp.mediaplayer.data.cache.room

import android.content.Context
import com.automotive.bootcamp.mediaplayer.data.cache.CacheAudioSource
import com.automotive.bootcamp.mediaplayer.data.cache.room.extensions.mapToEmbeddedPlaylistItem
import com.automotive.bootcamp.mediaplayer.data.cache.room.extensions.mapToPlaylistItem
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToEntity
import com.automotive.bootcamp.mediaplayer.data.models.AudioItem
import com.automotive.bootcamp.mediaplayer.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.mediaplayer.data.models.PlaylistItem

class RoomAudioSource(context: Context) : CacheAudioSource {
    private val dao = RoomAudioDatabase.getInstance(context).audioDao

    override suspend fun insertAudio(audio: AudioItem): Long {
        val audioEntity = audio.mapToEntity()

        return dao.insertAudio(audioEntity)
    }

    override suspend fun insertAudios(audios: List<AudioItem>): List<Long> {
        val audioEntities = audios.map {
            it.mapToEntity()
        }

        return dao.insertAudios(audioEntities)
    }

    override suspend fun playlistExists(pid: Long): Boolean {
        return dao.playlistExists(pid)
    }

    override suspend fun playlistHasAudio(pid: Long, aid: Long): Boolean {
        return dao.playlistHasAudio(pid, aid)
    }

    override suspend fun insertEmbeddedPlaylist(playlist: EmbeddedPlaylistItem) {
        dao.insertEmbeddedPlaylist(playlist.mapToEntity())
    }

    override suspend fun getEmbeddedPlaylist(name: String): EmbeddedPlaylistItem? {
        return dao.getEmbeddedPlaylist(name)?.mapToEmbeddedPlaylistItem()
    }

    override suspend fun insertPlaylist(playlist: PlaylistItem): Long {
        val playlistEntity = playlist.mapToEntity()

        return dao.insertPlaylist(playlistEntity)
    }

    override suspend fun deletePlaylist(pid: Long) {
        dao.deletePlaylist(pid)
    }

    override suspend fun insertAudioPlaylistCrossRef(crossRef: AudioPlaylistItemCrossRef) {
        val audioPlaylistCrossRefEntity = crossRef.mapToEntity()

        dao.insertAudioToPlaylist(audioPlaylistCrossRefEntity)
    }

    override suspend fun deleteAudioFromPlaylist(crossRef: AudioPlaylistItemCrossRef) {
        val audioPlaylistCrossRefEntity = crossRef.mapToEntity()

        dao.deleteAudioFromPlaylist(audioPlaylistCrossRefEntity)
    }

    override suspend fun getPlaylist(pid: Long): PlaylistItem? {
        return dao.getPlaylistWithAudios(pid)?.mapToPlaylistItem()
    }

    override suspend fun getAllPlaylists(): List<PlaylistItem>? {
        val playlists = dao.getAllPlaylistsWithAudios()

        playlists.let {
            return it?.map { playlist ->
                playlist.mapToPlaylistItem()
            }
        }

        return null
    }
}