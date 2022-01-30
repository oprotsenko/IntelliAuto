package com.automotive.bootcamp.mediaplayer.services

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.RatingCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat

class MediaPlayerService : MediaBrowserServiceCompat() {

    private val MEDIA_SERVICE_ID = "MediaPlayerServiceID"
    private var mediaSession: MediaSessionCompat? = null

    override fun onCreate() {
        super.onCreate()
        val callback = createCallback()
        mediaSession = MediaSessionCompat(baseContext, "MediaPlayerService").apply {
            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )
            val stateBuilder = PlaybackStateCompat.Builder().setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
                .setActions(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)
                .setActions(PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH)
                .setActions(PlaybackStateCompat.ACTION_PLAY_FROM_URI)
                .setActions(PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID)
                .setActions(PlaybackStateCompat.ACTION_PREPARE_FROM_URI)
                .setActions(PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH)
                .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                .setActions(PlaybackStateCompat.ACTION_SET_REPEAT_MODE)
                .setActions(PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE)
                .setActions(PlaybackStateCompat.ACTION_SKIP_TO_NEXT)
                .setActions(PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .setActions(PlaybackStateCompat.ACTION_SET_RATING)
            setPlaybackState(stateBuilder.build())
            setCallback(callback)
            setSessionToken(sessionToken)
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot(MEDIA_SERVICE_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        val mediaItems = mutableListOf<MediaBrowserCompat.MediaItem>()

        result.sendResult(mediaItems)
    }

    private fun createCallback(): MediaSessionCompat.Callback {
        return object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                super.onPlay()
            }

            override fun onPause() {
                super.onPause()
            }

            override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
                super.onPlayFromMediaId(mediaId, extras)
            }

            override fun onPlayFromSearch(query: String?, extras: Bundle?) {
                super.onPlayFromSearch(query, extras)
            }

            override fun onPlayFromUri(uri: Uri?, extras: Bundle?) {
                super.onPlayFromUri(uri, extras)
            }

            override fun onSeekTo(pos: Long) {
                super.onSeekTo(pos)
            }

            override fun onSetRepeatMode(repeatMode: Int) {
                super.onSetRepeatMode(repeatMode)
            }

            override fun onSetShuffleMode(shuffleMode: Int) {
                super.onSetShuffleMode(shuffleMode)
            }

            override fun onSkipToNext() {
                super.onSkipToNext()
            }

            override fun onSkipToPrevious() {
                super.onSkipToPrevious()
            }

            override fun onSetRating(rating: RatingCompat?) {
                super.onSetRating(rating)
            }
        }
    }
}