package com.automotive.bootcamp.music_service.service

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import com.automotive.bootcamp.music_service.service.callbacks.MusicPlaybackPreparer
import com.automotive.bootcamp.music_service.service.callbacks.MusicPlayerEventListener
import com.automotive.bootcamp.music_service.service.callbacks.MusicPlayerNotificationListener
import com.automotive.bootcamp.music_service.service.sources.BrowseTree
import com.automotive.bootcamp.music_service.service.sources.MusicSourceService
import com.automotive.bootcamp.music_service.service.utils.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSource
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

private const val SERVICE_TAG = "MusicServiceTag"

class MusicService : MediaBrowserServiceCompat() {

    private val player: ExoPlayer by inject()
    private val musicSource: MusicSourceService by inject()
//    private val defaultDataSource: DefaultDataSource.Factory by inject()

    private val browseTree: BrowseTree by lazy {
        BrowseTree(applicationContext, musicSource)
    }

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

//    private var currentAudio: MediaMetadataCompat? = null

    private lateinit var mediaSession: MediaSessionCompat
//    private lateinit var mediaSessionConnector: MediaSessionConnector
//    private lateinit var musicNotificationManager: MusicNotificationManager
//    private lateinit var musicPlayerEventListener: MusicPlayerEventListener

//    var isForegroundService = false

//    private var currentPlayingAudio: MediaMetadataCompat? = null
//    private var isPlayerInitialized = false

    private val callback = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
            val playbackState = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY)
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1f)
                .build()
            mediaSession.setPlaybackState(playbackState)
        }

//        override fun onSkipToQueueItem(queueId: Long) {}

//        override fun onSeekTo(position: Long) {}

//        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {}

        override fun onPause() {
            val playbackState = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PAUSE)
                .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1f)
                .build()
            mediaSession.setPlaybackState(playbackState)
        }

//        override fun onStop() {}

        override fun onSkipToNext() {
            val playbackState = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_SKIP_TO_NEXT)
                .setState(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT, 0, 1f)
                .build()
            mediaSession.setPlaybackState(playbackState)
        }

        override fun onSkipToPrevious() {
            val playbackState = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .setState(PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS, 0, 1f)
                .build()
            mediaSession.setPlaybackState(playbackState)
        }

//        override fun onCustomAction(action: String?, extras: Bundle?) {}

//        override fun onPlayFromSearch(query: String?, extras: Bundle?) {}
    }

    private val metadata = MediaMetadataCompat.Builder()
        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, "title").build()


    companion object {
        var currentAudioDuration = 0L
            private set
    }

    private fun preparePlayer(
        audios: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playNow: Boolean
    ) {
//        val currentAudioIndex =
//            if (currentPlayingAudio == null && !audios.isNullOrEmpty()) 0 else audios.indexOf(
//                itemToPlay
//            )
//        player.setMediaSource(musicSource.asMediaSource(defaultDataSource))
        player.prepare()
//        player.seekTo(currentAudioIndex, 0L)
        player.playWhenReady = playNow
    }

    override fun onCreate() {
        super.onCreate()
        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, 0)
        }
        mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }

        val playbackState = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY_PAUSE
                        or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            )
            .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1f)
            .build()
        mediaSession.setPlaybackState(playbackState)
        mediaSession.setMetadata(metadata)
        mediaSession.setCallback(callback)
        sessionToken = mediaSession.sessionToken
        serviceScope.launch {
            musicSource.load()
        }

//        musicNotificationManager = MusicNotificationManager(
//            this,
//            mediaSession.sessionToken,
//            MusicPlayerNotificationListener(this)
//        ) {
//            currentAudioDuration = player.duration
//        }

//        serviceScope.launch {
//            musicSource.retrieveLocalAudio()
//            musicSource.retrieveRemoteAudio()
//            musicSource.retrieveRecentAudio()
//            musicSource.retrieveFavouriteAudio()
//        }

//        val musicPlaybackPreparer = MusicPlaybackPreparer(musicSource) {
//            currentPlayingAudio = it
//            preparePlayer(musicSource.audios, it, true)
//        }

//        mediaSessionConnector = MediaSessionConnector(mediaSession)
//        mediaSessionConnector.setPlaybackPreparer(musicPlaybackPreparer)
//        mediaSessionConnector.setQueueNavigator(MusicQueueNavigator())
//        mediaSessionConnector.setPlayer(player)

//        musicPlayerEventListener = MusicPlayerEventListener(this)
//        player.addListener(musicPlayerEventListener)
//        musicNotificationManager.showNotification(player)
    }

//    override fun onTaskRemoved(rootIntent: Intent?) {
//        super.onTaskRemoved(rootIntent)
//        player.stop()
//    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()

//        player.removeListener(musicPlayerEventListener)
        player.release()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(BROWSABLE_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        val resultSent = musicSource.whenReady { successfullyInitialized ->
            if (successfullyInitialized) {
                val children = browseTree[parentId]?.map {
                    MediaBrowserCompat.MediaItem(
                        MediaDescriptionCompat.Builder()
                            .setTitle(it.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
                            .setMediaId(it.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID))
                            .setDescription(it.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
                            .build(), it.getLong(METADATA_KEY_FLAGS).toInt()
                    )
                }?.toMutableList()
                result.sendResult(children)
            } else {
                mediaSession.sendSessionEvent(NETWORK_ERROR, null)
                result.sendResult(null)
            }
        }
        if (!resultSent) {
            result.detach()
        }
    }

//    private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSession) {
//        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
//            return musicSource.audios[windowIndex].description
//        }
//    }
}