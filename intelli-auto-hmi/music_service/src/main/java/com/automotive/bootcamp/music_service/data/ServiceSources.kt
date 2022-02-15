package com.automotive.bootcamp.music_service.data

import android.media.browse.MediaBrowser
import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import android.util.Log
import com.automotive.bootcamp.music_service.data.models.AudioItem
import com.automotive.bootcamp.music_service.data.models.PlaylistItem
import com.automotive.bootcamp.music_service.data.remote.RemoteAudioSource
import com.automotive.bootcamp.music_service.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ServiceSources : AbstractMusicSource(), KoinComponent {

    private val localRepository: LocalMediaRepository by inject()
    private val remoteSource: RemoteAudioSource by inject()
    private val cacheRepository: CacheMediaRepository by inject()

    val sourceList = mutableListOf<MediaMetadataCompat>()

    init {
        Log.d("serviceTAG", "state initializing ")
        state = State.INITIALIZING
    }

    override fun iterator(): Iterator<MediaMetadataCompat> = sourceList.iterator()

    override suspend fun load() {
        retrieveLocalAudio()
        Log.d("serviceTAG", "local done")
        retrieveRemoteAudio()
        Log.d("serviceTAG", "remote done")
        retrieveCacheAudio()
        Log.d("serviceTAG", "cache done")
        state = State.INITIALIZED
    }

    private fun retrieveCacheAudio() {
        val playlists = cacheRepository.getAllPlaylists()
        Log.d("serviceTAG", "playlists loaded")
        val list = playlists.mapToMediaMetadataCompat()
        list.forEach {
            Log.d("serviceTAGA", "playlist " + it.getString(METADATA_KEY_MEDIA_ID))
            sourceList.add(it)
        }
    }

    private suspend fun retrieveRemoteAudio() {
        val audios = remoteSource.load()
        audios?.let { cacheRepository.insertAudios(audios) }
        val metadataCompat = audios?.mapToMediaMetadataCompat(REMOTE_ROOT_ID, true)
        metadataCompat?.forEach {
            sourceList.add(it)
        }
    }

    private suspend fun retrieveLocalAudio() {
        val audios = localRepository.retrieveLocalAudio()
        cacheRepository.insertAudios(audios)
        val metadataCompat = audios.mapToMediaMetadataCompat(LOCAL_ROOT_ID)
        metadataCompat.forEach {
            sourceList.add(it)
        }
    }

    private fun List<AudioItem>.mapToMediaMetadataCompat(
        rootId: String,
        isRemote: Boolean = false
    ): List<MediaMetadataCompat> =
        this.map { audio ->
            val imageUri = AlbumArtContentProvider.mapUri(Uri.parse(audio.cover), isRemote)
            Builder()
                .putString(METADATA_KEY_MEDIA_ID, audio.id.toString())
                .putString(METADATA_KEY_ARTIST, audio.artist)
                .putString(METADATA_KEY_TITLE, audio.title)
                .putString(METADATA_KEY_ART_URI, imageUri.toString())
                .putString(METADATA_KEY_MEDIA_URI, audio.url)
                .putString(METADATA_KEY_DISPLAY_TITLE, audio.title)
                .putString(METADATA_KEY_DISPLAY_SUBTITLE, audio.artist)
                .putString(METADATA_KEY_DISPLAY_ICON_URI, imageUri.toString())
                .putLong(METADATA_KEY_FLAGS, MediaBrowser.MediaItem.FLAG_PLAYABLE.toLong())
                .putString(METADATA_KEY_ALBUM, rootId)
                .build()
        }

    private fun Flow<List<PlaylistItem>?>.mapToMediaMetadataCompat(): List<MediaMetadataCompat> {
        Log.d("serviceTAG", "map playlist")
        val mediaMetadata = mutableListOf<MediaMetadataCompat>()
        this.map { list ->
            list?.map { playlistItem ->
                mediaMetadata.add(playlistItem.mapPlaylistToMediaMetadataCompat())
            }
        }
        this.map { list ->
            var metadataList: List<MediaMetadataCompat>? = null
            list?.map { playlistItem ->
                metadataList = playlistItem.list?.mapToMediaMetadataCompat(playlistItem.name)
            }
            metadataList?.forEach {
                mediaMetadata.add(it)
            }
        }
        return mediaMetadata
    }

    private fun PlaylistItem.mapPlaylistToMediaMetadataCompat(): MediaMetadataCompat =
        Builder()
            .putString(METADATA_KEY_MEDIA_ID, this.name)
            .putString(METADATA_KEY_TITLE, this.name)
            .putString(METADATA_KEY_ALBUM, ALBUMS_ROOT_ID)
            .putLong(METADATA_KEY_FLAGS, MediaBrowser.MediaItem.FLAG_BROWSABLE.toLong())
            .build()

    suspend fun addToRecent(aid: Long) {
        cacheRepository.addToRecent(aid)
//        state = State.INITIALIZING
//        sourceList.clear()
//        load()
    }
}
