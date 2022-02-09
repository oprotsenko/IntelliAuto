package com.automotive.bootcamp.music_service.service.sources

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.automotive.bootcamp.music_service.data.PlaylistMediaRepository
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

class PlaylistsServiceSource(
    private val playlistRepository: PlaylistMediaRepository,
) : AbstractMusicSource() {

    var playlists = emptyList<MediaMetadataCompat>()

    override fun iterator(): Iterator<MediaMetadataCompat> = playlists.iterator()

    override suspend fun load() {
        state = State.INITIALIZING
        val data = playlistRepository.getAllPlaylists()
//        audios = data.map { audio ->
//            MediaMetadataCompat.Builder()
//                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, audio.id.toString())
//                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, audio.artist)
//                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, audio.title)
//                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, audio.cover)
//                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, audio.url)
//                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, audio.title)
//                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, audio.artist)
//                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, audio.cover)
//                .build()
//        }
        state = State.INITIALIZED
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        playlists.forEach { audio ->
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(audio.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = playlists.map { audio ->
        val description = MediaDescriptionCompat.Builder()
            .setMediaId(audio.description.mediaId)
            .setTitle(audio.description.title)
            .setSubtitle(audio.description.subtitle)
            .setIconUri(audio.description.iconUri)
            .setMediaUri(audio.description.mediaUri)
            .build()
        MediaBrowserCompat.MediaItem(description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
    }.toMutableList()
}