package com.automotive.bootcamp.music_service.data.cache

import android.util.Log
import com.automotive.bootcamp.music_service.data.CacheMediaRepository
import com.automotive.bootcamp.music_service.data.cache.room.RoomAudioSource
import com.automotive.bootcamp.music_service.data.models.AudioItem
import com.automotive.bootcamp.music_service.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.music_service.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.music_service.data.models.PlaylistItem
import com.automotive.bootcamp.music_service.utils.FAVOURITE_PLAYLIST_NAME
import com.automotive.bootcamp.music_service.utils.RECENT_PLAYLIST_CAPACITY
import com.automotive.bootcamp.music_service.utils.RECENT_PLAYLIST_NAME
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CacheRepository(private val cacheAudioSource: RoomAudioSource) : CacheMediaRepository {
    private var pidRecent: Long? = null
    private var pidFavourite: Long? = null

    private val job = Job()
    private val repositoryScope = CoroutineScope(Dispatchers.IO + job)

    init {
        repositoryScope.launch {
            if (getRecentPlaylist() == null) {
                createRecentPlaylist()
                Log.d("allPlaylists", "recent created " + pidRecent)
            } else {
                pidRecent = getRecentPlaylist()?.id
                Log.d("allPlaylists", "recent exist " + pidRecent)
            }
            if (getFavouritePlaylist() == null) {
                createFavouritePlaylist()
                Log.d("allPlaylists", "favourite created " + pidFavourite)
            } else {
                pidFavourite = getFavouritePlaylist()?.id
                Log.d("allPlaylists", "favourite exist " + pidFavourite)
            }
        }
    }

    override fun getAllPlaylists(): Flow<List<PlaylistItem>?> =
        cacheAudioSource.getAllPlaylists()

    override suspend fun insertAudios(audios: List<AudioItem>): List<Long> =
        cacheAudioSource.insertAudios(audios)

    override suspend fun addToRecent(aid: Long) {
        if (pidRecent == null) {
            createRecentPlaylist()
        }
        pidRecent?.let {
            if (!cacheAudioSource.playlistHasAudio(it, aid)) {
                checkPlaylistCapacity()
                val count = cacheAudioSource.insertAudioPlaylistCrossRef(AudioPlaylistItemCrossRef(aid, it))
                Log.d("CacheRepository", "addToRecent -> $count")
            }
        }
    }

    override suspend fun addToFavourite(aid: Long) {
        if (pidFavourite == null) {
            createFavouritePlaylist()
        }
        pidFavourite?.let { pid ->
            Log.d("CacheRepository", "aid -> $pid")
            if (!cacheAudioSource.playlistHasAudio(pid, aid)) {
                val count = cacheAudioSource.insertAudioPlaylistCrossRef(AudioPlaylistItemCrossRef(aid, pid))
                Log.d("CacheRepository", "addToFavourite -> $count")
            } else {
                val count = cacheAudioSource.deleteAudioFromPlaylist(AudioPlaylistItemCrossRef(aid, pid))
                Log.d("CacheRepository", "deleteFromFavourite -> $count")
            }
        }
    }

    override suspend fun isFavourite(aid: Long): Boolean {
        pidFavourite?.let { pid ->
            return cacheAudioSource.playlistHasAudio(pid, aid)
        }
        return false
    }

    private suspend fun checkPlaylistCapacity() {
        pidRecent?.let { pid ->
            val recentAudios = cacheAudioSource.getPlaylist(pid).map { playlist ->
                playlist?.list
            }
            recentAudios.let { flow ->
                flow.map { list ->
                    list?.let { audios ->
                        if (audios.size >= RECENT_PLAYLIST_CAPACITY) {
                            val aid = audios.minOf { it.id }
                            val crossRef = AudioPlaylistItemCrossRef(aid, pid)
                            cacheAudioSource.deleteAudioFromPlaylist(crossRef)
                        }
                    }
                }
            }
        }
    }

    private suspend fun createRecentPlaylist() {
        val recentPlaylist = PlaylistItem(name = RECENT_PLAYLIST_NAME, list = null)
        val recentPlaylistId = cacheAudioSource.insertPlaylist(recentPlaylist)
        val embeddedPlaylistItem = EmbeddedPlaylistItem(recentPlaylistId, RECENT_PLAYLIST_NAME)
        cacheAudioSource.insertEmbeddedPlaylist(embeddedPlaylistItem)
        pidRecent = recentPlaylistId
    }

    private suspend fun createFavouritePlaylist() {
        val favouritePlaylist = PlaylistItem(name = FAVOURITE_PLAYLIST_NAME, list = null)
        val favouritePlaylistId = cacheAudioSource.insertPlaylist(favouritePlaylist)
        val embeddedPlaylistItem = EmbeddedPlaylistItem(favouritePlaylistId, FAVOURITE_PLAYLIST_NAME)
        cacheAudioSource.insertEmbeddedPlaylist(embeddedPlaylistItem)
        pidFavourite = favouritePlaylistId
    }

    private suspend fun getRecentPlaylist(): EmbeddedPlaylistItem? =
        cacheAudioSource.getEmbeddedPlaylist(RECENT_PLAYLIST_NAME)

    private suspend fun getFavouritePlaylist(): EmbeddedPlaylistItem? =
        cacheAudioSource.getEmbeddedPlaylist(FAVOURITE_PLAYLIST_NAME)
}