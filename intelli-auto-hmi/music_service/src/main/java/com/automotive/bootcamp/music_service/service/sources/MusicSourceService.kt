package com.automotive.bootcamp.music_service.service.sources

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.automotive.bootcamp.music_service.data.*
import com.automotive.bootcamp.music_service.service.utils.FAVOURITE_ROOT_ID
import com.automotive.bootcamp.music_service.service.utils.LOCAL_ROOT_ID
import com.automotive.bootcamp.music_service.service.utils.RECENT_ROOT_ID
import com.automotive.bootcamp.music_service.service.utils.REMOTE_ROOT_ID
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class MusicSourceService(
    private val localRepository: LocalMediaRepository,
    private val remoteRepository: RemoteMediaRepository,
    private val favouriteRepository: FavouriteMediaRepository,
    private val recentRepository: RecentMediaRepository,
    private val playlistRepository: PlaylistMediaRepository,
) : AbstractMusicSource() {

    var audios = emptyList<MediaMetadataCompat>()

    override fun iterator(): Iterator<MediaMetadataCompat> = audios.iterator()

    suspend fun retrieveLocalAudio() {
        state = State.INITIALIZING
        audios = localRepository.retrieveLocalAudio().map { audio ->
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, audio.id.toString())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, audio.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, audio.title)
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, audio.cover)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, audio.url)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, audio.title)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, audio.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, audio.cover)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, LOCAL_ROOT_ID)
                .build()
        }
        state = State.INITIALIZED
    }

    suspend fun retrieveRemoteAudio() {
        state = State.INITIALIZING
        val data = remoteRepository.retrieveRemoteAudio()
        if (data != null) {
            audios = data.map { audio ->
                MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, audio.id.toString())
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, audio.artist)
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, audio.title)
                    .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, audio.cover)
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, audio.url)
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, audio.title)
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, audio.artist)
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, audio.cover)
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, audio.title)
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, REMOTE_ROOT_ID)
                    .build()
            }
            state = State.INITIALIZED
        }
    }

    suspend fun retrieveRecentAudio() {
        state = State.INITIALIZING
        val data = recentRepository.getPlaylist()?.map { playlist ->
            playlist?.list
        }?.toList()?.get(0)

        if (data != null) {
            audios = data.map { audio ->
                MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, audio.id.toString())
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, audio.artist)
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, audio.title)
                    .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, audio.cover)
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, audio.url)
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, audio.title)
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, audio.artist)
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, audio.cover)
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, RECENT_ROOT_ID)
                    .build()
            }
            state = State.INITIALIZED
        }
    }

    suspend fun retrieveFavouriteAudio() {
        state = State.INITIALIZING
        val data = favouriteRepository.getPlaylist()?.map { playlist ->
            playlist?.list
        }?.toList()?.get(0)

        if (data != null) {
            audios = data.map { audio ->
                MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, audio.id.toString())
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, audio.artist)
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, audio.title)
                    .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, audio.cover)
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, audio.url)
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, audio.title)
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, audio.artist)
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, audio.cover)
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, FAVOURITE_ROOT_ID)
                    .build()
            }
            state = State.INITIALIZED
        }
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): ConcatenatingMediaSource {
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