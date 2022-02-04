package com.automotive.bootcamp.music_service.data

import com.automotive.bootcamp.music_service.data.cache.CacheAudioSource
import com.automotive.bootcamp.music_service.data.extensions.mapToPlaylist
import com.automotive.bootcamp.music_service.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.music_service.service.models.Playlist
import com.automotive.bootcamp.music_service.service.models.extensions.mapToPlaylistItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlaylistRepository(
    private val cacheAudioSource: CacheAudioSource,
    private val dispatcher: CoroutineDispatcher
) : PlaylistMediaRepository{
    override suspend fun playlistExists(pid: Long): Boolean =
        withContext(dispatcher) {
            cacheAudioSource.playlistExists(pid)
        }

    override suspend fun playlistHasAudio(pid: Long, aid: Long): Boolean =
        withContext(dispatcher) {
            cacheAudioSource.playlistHasAudio(pid, aid)
        }

    override suspend fun addPlaylist(playlist: Playlist): Long =
        withContext(dispatcher) {
            cacheAudioSource.insertPlaylist(playlist.mapToPlaylistItem())
        }

    override suspend fun removePlaylist(pid: Long) = withContext(dispatcher) {
        cacheAudioSource.deletePlaylist(pid)
    }

    override suspend fun addAudioToPlaylist(crossRef: AudioPlaylistItemCrossRef) = withContext(dispatcher) {
        cacheAudioSource.insertAudioPlaylistCrossRef(crossRef)
    }

    override suspend fun removeAudioFromPlaylist(crossRef: AudioPlaylistItemCrossRef) =
        withContext(dispatcher) {
            cacheAudioSource.deleteAudioFromPlaylist(crossRef)
        }

    override fun getPlaylist(pid: Long): Flow<Playlist?> {
        return cacheAudioSource.getPlaylist(pid).map {
            it?.mapToPlaylist()
        }.flowOn(dispatcher)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>?> {
        return cacheAudioSource.getAllPlaylists().map { playlists ->
            playlists?.map { playlist ->
                playlist.mapToPlaylist()
            }
        }.flowOn(dispatcher)
    }
}