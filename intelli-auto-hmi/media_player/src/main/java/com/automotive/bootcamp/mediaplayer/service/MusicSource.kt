package com.automotive.bootcamp.mediaplayer.service

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.core.net.toUri
import com.automotive.bootcamp.mediaplayer.domain.*
import com.automotive.bootcamp.mediaplayer.service.State.CREATED
import com.automotive.bootcamp.mediaplayer.service.State.INITIALIZING
import com.automotive.bootcamp.mediaplayer.service.State.INITIALIZED
import com.automotive.bootcamp.mediaplayer.service.State.ERROR
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

class MusicSource(
    private val localRepository: LocalMediaRepository,
    private val remoteRepository: RemoteMediaRepository,
    private val favouriteRepository: FavouriteMediaRepository,
    private val recentRepository: RecentMediaRepository,
    private val playlistRepository: PlaylistMediaRepository,
) {

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    var audios = emptyList<MediaMetadataCompat>()

    private var state: State = CREATED
        set(value) {
            if (value == INITIALIZED || value == ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    suspend fun retrieveLocalAudio() {
        state = INITIALIZING
        audios = localRepository.retrieveLocalAudio().map { audio ->
            MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, audio.id)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, audio.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_DURATION, audio.duration)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, audio.title)
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, audio.cover)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, audio.url)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, audio.title)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, audio.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, audio.cover)
                .build()
        }
        state = INITIALIZED
    }

//    suspend fun retrieveRemoteAudio
//    suspend fun retrieveFavouriteAudio
//    suspend fun retrieveRecentAudio
//    suspend fun retrievePlaylists

    fun whenReady(action: (Boolean) -> Unit): Boolean {
        return if (state == CREATED || state == INITIALIZING) {
            onReadyListeners += action
            false
        } else {
            action(state == INITIALIZED)
            true
        }
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory) : ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        audios.forEach { audio ->
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(audio.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = audios.map { audio ->
        val description = MediaDescriptionCompat.Builder()
            .setMediaId(audio.description.mediaId)
            .setTitle(audio.description.title)
            .setSubtitle(audio.description.subtitle)
            .setIconUri(audio.description.iconUri)
            .setMediaUri(audio.description.mediaUri)
            .build()
        MediaBrowserCompat.MediaItem(description, FLAG_PLAYABLE)
    }.toMutableList()
}

enum class State {
    CREATED,
    INITIALIZING,
    INITIALIZED,
    ERROR
}