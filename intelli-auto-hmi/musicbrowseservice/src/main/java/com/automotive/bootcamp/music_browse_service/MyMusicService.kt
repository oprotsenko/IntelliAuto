package com.automotive.bootcamp.music_browse_service

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.automotive.bootcamp.music_browse_service.callbacks.MusicPlaybackPreparer
import com.automotive.bootcamp.music_browse_service.callbacks.MusicPlayerEventListener
import com.automotive.bootcamp.music_browse_service.callbacks.MusicPlayerNotificationListener
import com.automotive.bootcamp.music_browse_service.sources.ServiceSources
import com.automotive.bootcamp.music_browse_service.utils.BROWSABLE_ROOT_ID
import com.automotive.bootcamp.music_browse_service.utils.METADATA_KEY_FLAGS
import com.automotive.bootcamp.music_browse_service.utils.NETWORK_ERROR
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyMusicService : MediaBrowserServiceCompat() {
    private lateinit var notificationManager: MusicNotificationManager

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var sessionConnector: MediaSessionConnector

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private val musicSource = ServiceSources(this)
    private val tree by lazy { BrowseTree(this, musicSource) }

    var isForegroundService = false

    private var curPlayingSong: MediaMetadataCompat? = null

    private val serviceAudioAttributes = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    private val playerListener = MusicPlayerEventListener(this)

    private val exoPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(this).build().apply {
            setAudioAttributes(serviceAudioAttributes, true)
            setHandleAudioBecomingNoisy(true)
            addListener(playerListener)
        }
    }

    private val dataSourceFactory by lazy {
        DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, "Bootcamp App")
        )
    }

    companion object {
        var currentAudioDuration = 0L
            private set
    }

    override fun onCreate() {
        super.onCreate()
        serviceScope.launch {
            musicSource.load()
        }
        // Build a PendingIntent that can be used to launch the UI.
        val sessionActivityPendingIntent =
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(this, 0, sessionIntent, 0)
            }

        Log.d("serviceTAG", "onCreate")

        mediaSession = MediaSessionCompat(this, "MyMusicService").apply {
            setSessionActivity(sessionActivityPendingIntent)
            isActive = true
        }
        val playbackState = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY_PAUSE
                        or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            )
        mediaSession.setPlaybackState(playbackState.build())

        sessionToken = mediaSession.sessionToken

        notificationManager = MusicNotificationManager(
            this,
            mediaSession.sessionToken,
            MusicPlayerNotificationListener(this)
        ) {
            currentAudioDuration = exoPlayer.duration
        }

        val musicPlaybackPreparer = MusicPlaybackPreparer(musicSource) {
            Log.d("serviceTAG", it?.description?.title.toString())

            curPlayingSong = it
            preparePlayer(
                musicSource.music,
                it,
                true
            )
        }

        sessionConnector = MediaSessionConnector(mediaSession)
        sessionConnector.setPlaybackPreparer(musicPlaybackPreparer)
        sessionConnector.setQueueNavigator(AudioQueueNavigator())

        notificationManager.showNotification(exoPlayer)
    }

    private inner class AudioQueueNavigator : TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return musicSource.music[windowIndex].description
        }
    }

    private fun preparePlayer(
        songs: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playNow: Boolean
    ) {
        val curSongIndex = if (curPlayingSong == null) 0 else songs.indexOf(itemToPlay)

        Log.d("serviceTAG", "preparePlayer -> $curSongIndex")

        exoPlayer.prepare(musicSource.asMediaSource(dataSourceFactory))
        exoPlayer.seekTo(curSongIndex, 0L)
        exoPlayer.playWhenReady = playNow
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
    }
    override fun onDestroy() {
        mediaSession.run {
            isActive = false
            release()
        }
        serviceJob.cancel()
        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        Log.d("serviceTAG", "onGetRoot")
        return BrowserRoot(BROWSABLE_ROOT_ID, null)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaItem>>) {
        Log.d("serviceTAG", "onLoadChildren " + parentId)
        val resultSent = musicSource.whenReady { initialized ->
            if (initialized && tree[parentId]?.isNotEmpty() == true) {
                Log.d("serviceTAG", "songs count " + tree[parentId]?.size)
                val treeItem = tree[parentId]?.map {
                    Log.d("serviceTAG", "children item " + it.getString(METADATA_KEY_TITLE))
                    MediaItem(
                        MediaDescriptionCompat.Builder()
                            .setTitle(it.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
                            .setMediaId(it.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID))
                            .setDescription(it.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
//                            .setIconUri(localIconUri)
                            .build(), it.getLong(METADATA_KEY_FLAGS).toInt()
                    )
                }?.toMutableList()
                Log.d("serviceTAG", "treeItem " + treeItem + " size " + treeItem?.size)
                result.sendResult(treeItem)
            } else {
                mediaSession.sendSessionEvent(NETWORK_ERROR, null)
                result.sendResult(null)
            }
        }
        if (!resultSent) {
            result.detach()
        }
    }
}

const val MEDIA_SEARCH_SUPPORTED = "android.media.browse.SEARCH_SUPPORTED"
private const val CONTENT_STYLE_BROWSABLE_HINT = "android.media.browse.CONTENT_STYLE_BROWSABLE_HINT"
private const val CONTENT_STYLE_PLAYABLE_HINT = "android.media.browse.CONTENT_STYLE_PLAYABLE_HINT"
private const val CONTENT_STYLE_SUPPORTED = "android.media.browse.CONTENT_STYLE_SUPPORTED"
private const val CONTENT_STYLE_LIST = 1
private const val CONTENT_STYLE_GRID = 2