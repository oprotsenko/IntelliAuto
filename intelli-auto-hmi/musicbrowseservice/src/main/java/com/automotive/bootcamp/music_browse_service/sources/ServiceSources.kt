package com.automotive.bootcamp.music_browse_service.sources

import android.content.Context
import android.media.MediaMetadataRetriever
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import com.automotive.bootcamp.music_browse_service.utils.LOCAL_ROOT_ID

class ServiceSources(
    context: Context
) : AbstractMusicSource() {

    private val localSource = LocalAudioSource(context)

    private val music = mutableListOf<MediaMetadataCompat>()

    init {
        state = State.INITIALIZING
    }

    override fun load() {
        Log.d("serviceTAG", "loading")
        val audios = localSource.retrieveLocalAudio().map { audio ->
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
        audios.forEach {
            music.add(it)
        }
        state = State.INITIALIZED
        Log.d("serviceTAG", music.size.toString())
    }

    override fun iterator(): Iterator<MediaMetadataCompat> = music.iterator()
}