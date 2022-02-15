package com.automotive.bootcamp.music_service.data.cache.room

import android.content.Context
import android.util.Log
import com.automotive.bootcamp.music_service.data.cache.CacheMediaSource
import com.automotive.bootcamp.music_service.data.cache.room.extensions.*
import com.automotive.bootcamp.music_service.data.models.AudioItem
import com.automotive.bootcamp.music_service.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.music_service.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.music_service.data.models.PlaylistItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomAudioSource(context: Context) : CacheMediaSource {
    private val dao = RoomAudioDatabase.getInstance(context).audioDao

    override suspend fun insertAudio(audio: AudioItem): Long =
        dao.insertAudio(audio.mapToAudioEntity())

    override suspend fun insertAudios(audios: List<AudioItem>): List<Long> =
        dao.insertAudios(audios.map { it.mapToAudioEntity() })

    override suspend fun playlistExists(pid: Long): Boolean =
        dao.playlistExists(pid)

    override suspend fun playlistHasAudio(pid: Long, aid: Long): Boolean =
        dao.playlistHasAudio(pid, aid) > 0

    override suspend fun insertEmbeddedPlaylist(playlist: EmbeddedPlaylistItem) =
        dao.insertEmbeddedPlaylist(playlist.mapToEmbeddedPlaylistEntity())

    override suspend fun insertPlaylist(playlist: PlaylistItem): Long =
        dao.insertPlaylist(playlist.mapToPlaylistEntity())

    override suspend fun deletePlaylist(pid: Long) =
        dao.deletePlaylist(pid)

    override suspend fun insertAudioPlaylistCrossRef(crossRef: AudioPlaylistItemCrossRef) =
        dao.insertAudioToPlaylist(crossRef.mapToAudioPlaylistCrossRefEntity())

    override suspend fun deleteAudioFromPlaylist(crossRef: AudioPlaylistItemCrossRef) =
        dao.deleteAudioFromPlaylist(crossRef.mapToAudioPlaylistCrossRefEntity())

    override suspend fun getEmbeddedPlaylist(name: String): EmbeddedPlaylistItem? =
        dao.getEmbeddedPlaylist(name)?.mapToEmbeddedPlaylistItem()

    override suspend fun getPlaylistSize(pid: Long): Int =
        dao.getPlaylistSize(pid)

    override fun getPlaylist(pid: Long): Flow<PlaylistItem?> =
        dao.getPlaylistWithAudios(pid).map { playlistWithAudios ->
            playlistWithAudios?.mapToPlaylistItem()
        }

    override fun getAllPlaylists(): Flow<List<PlaylistItem>?> =
        dao.getAllPlaylistsWithAudios().map { list ->
            Log.d("serviceTAG", "getting form dao")
            list?.map { playlistWithAudios ->
                playlistWithAudios.mapToPlaylistItem()
            }
        }
}