package com.automotive.bootcamp.music_service.data.cache

import com.automotive.bootcamp.music_service.data.CacheMediaRepository
import com.automotive.bootcamp.music_service.data.cache.room.RoomAudioSource
import com.automotive.bootcamp.music_service.data.models.AudioItem
import com.automotive.bootcamp.music_service.data.models.AudioPlaylistItemCrossRef
import com.automotive.bootcamp.music_service.data.models.EmbeddedPlaylistItem
import com.automotive.bootcamp.music_service.data.models.PlaylistItem
import com.automotive.bootcamp.music_service.utils.RECENT_PLAYLIST_CAPACITY
import com.automotive.bootcamp.music_service.utils.RECENT_PLAYLIST_NAME
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class CacheRepository(private val cacheAudioSource: RoomAudioSource) : CacheMediaRepository {
    private var pidRecent: Long? = null

    private val job = Job()
    private val repositoryScope = CoroutineScope(Dispatchers.Main + job)

    init {
        repositoryScope.launch {
            pidRecent = getRecentPlaylist()?.id
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
                cacheAudioSource.insertAudioPlaylistCrossRef(AudioPlaylistItemCrossRef(aid, it))
            }
        }
    }

    override suspend fun getRecentAudios(): List<AudioItem>? {
        var audios: List<AudioItem>? = null
        pidRecent?.let {
            val playlist = cacheAudioSource.getPlaylist(it)
            val res = playlist.map { playlistItem ->
                playlistItem?.list
            }.toList()
            audios = res[0]
        }
        return audios
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

    private suspend fun getRecentPlaylist(): EmbeddedPlaylistItem? =
        cacheAudioSource.getEmbeddedPlaylist(RECENT_PLAYLIST_NAME)
}