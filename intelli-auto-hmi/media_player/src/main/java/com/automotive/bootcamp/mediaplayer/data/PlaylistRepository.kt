package com.automotive.bootcamp.mediaplayer.data

import com.automotive.bootcamp.mediaplayer.data.cache.CacheMediaSource
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToAudioPlaylistItemCrossRef
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToPlaylist
import com.automotive.bootcamp.mediaplayer.data.extensions.mapToPlaylistItem
import com.automotive.bootcamp.mediaplayer.domain.PlaylistMediaRepository
import com.automotive.bootcamp.mediaplayer.domain.models.AudioPlaylistCrossRef
import com.automotive.bootcamp.mediaplayer.domain.models.Playlist
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlaylistRepository(
    private val cacheMediaSource: CacheMediaSource,
    private val dispatcher: CoroutineDispatcher
) : PlaylistMediaRepository{
    override suspend fun playlistExists(pid: Long): Boolean =
        withContext(dispatcher) {
            cacheMediaSource.playlistExists(pid)
        }

    override suspend fun playlistHasAudio(pid: Long, aid: Long): Boolean =
        withContext(dispatcher) {
            cacheMediaSource.playlistHasAudio(pid, aid)
        }

    override suspend fun addPlaylist(playlist: Playlist): Long =
        withContext(dispatcher) {
            cacheMediaSource.insertPlaylist(playlist.mapToPlaylistItem())
        }

    override suspend fun removePlaylist(pid: Long) = withContext(dispatcher) {
        cacheMediaSource.deletePlaylist(pid)
    }

    override suspend fun addAudioToPlaylist(crossRef: AudioPlaylistCrossRef) = withContext(dispatcher) {
        cacheMediaSource.insertAudioPlaylistCrossRef(crossRef.mapToAudioPlaylistItemCrossRef())
    }

    override suspend fun removeAudioFromPlaylist(crossRef: AudioPlaylistCrossRef) =
        withContext(dispatcher) {
            cacheMediaSource.deleteAudioFromPlaylist(crossRef.mapToAudioPlaylistItemCrossRef())
        }

    override fun getPlaylist(pid: Long): Flow<Playlist?> {
        return cacheMediaSource.getPlaylist(pid).map {
            it?.mapToPlaylist()
        }.flowOn(dispatcher)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>?> {
        return cacheMediaSource.getAllPlaylists().map { playlists ->
            playlists?.map { playlist ->
                playlist.mapToPlaylist()
            }
        }.flowOn(dispatcher)
    }
}