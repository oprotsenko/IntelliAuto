package com.automotive.bootcamp.music_service.service

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.media.MediaBrowserServiceCompat
import com.automotive.bootcamp.music_service.R
import com.automotive.bootcamp.music_service.data.ServiceSources
import com.automotive.bootcamp.music_service.utils.BROWSABLE_ROOT_ID
import com.automotive.bootcamp.music_service.utils.METADATA_KEY_FLAGS
import com.automotive.bootcamp.music_service.utils.NETWORK_ERROR
import com.automotive.bootcamp.music_service.utils.toMediaItem
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.upstream.BaseDataSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MusicService : MediaBrowserServiceCompat() {

    //    private lateinit var notificationManager: AudioNotificationManager
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var sessionConnector: MediaSessionConnector

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private val musicSource = ServiceSources(this)
    private val tree by lazy { BrowseTree(musicSource) }

    private val playerListener = AudioPlayerEventListener()
    private val serviceAudioAttributes = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()
    private val exoPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(this).build().apply {
            setAudioAttributes(serviceAudioAttributes, true)
            setHandleAudioBecomingNoisy(true)
            addListener(playerListener)
        }
    }
    private val dataSourceFactory by lazy {
        DefaultDataSource.Factory(this)
    }

    private var curPlayingSong: MediaMetadataCompat? = null
    private var currentPlaylistItems: List<MediaMetadataCompat> = emptyList()
    private var currentMediaItemIndex: Int = 0

    var isForegroundService = false

//    companion object {
//        var currentAudioDuration = 0L
//            private set
//    }

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
        sessionToken = mediaSession.sessionToken
//        notificationManager = AudioNotificationManager(
//            this,
//            mediaSession.sessionToken,
//            MusicPlayerNotificationListener(this)
//        ) {
//            currentAudioDuration = exoPlayer.duration
//        }
//        val playbackState = PlaybackStateCompat.Builder()
//            .setActions(
//                PlaybackStateCompat.ACTION_PLAY_PAUSE
//                        or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
//                        or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
//            )
//        mediaSession.setPlaybackState(playbackState.build())

        sessionConnector = MediaSessionConnector(mediaSession)
        sessionConnector.setPlayer(exoPlayer)
        sessionConnector.setPlaybackPreparer(MusicPlaybackPreparer())
        sessionConnector.setQueueNavigator(AudioQueueNavigator())

//        notificationManager.showNotification(exoPlayer)
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
        val rootExtras = Bundle().apply {
//            putBoolean(CONTENT_STYLE_SUPPORTED, true)
        }
        return BrowserRoot(BROWSABLE_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        Log.d("serviceTAG", "onLoadChildren " + parentId)
        val resultSent = musicSource.whenReady { initialized ->
            if (initialized && tree[parentId]?.isNotEmpty() == true) {
                Log.d("serviceTAG", "songs count " + tree[parentId]?.size)
                val treeItem = tree[parentId]?.map {
                    Log.d("serviceTAG", "children item " + it.getString(METADATA_KEY_TITLE))
                    Log.d("serviceTAG", "cover url " + it.getString(METADATA_KEY_ART_URI))
                    if (it.getLong(METADATA_KEY_FLAGS) == FLAG_BROWSABLE.toLong()) {
                        MediaBrowserCompat.MediaItem(
                            MediaDescriptionCompat.Builder()
                                .setTitle(it.getString(METADATA_KEY_TITLE))
                                .setMediaId(it.getString(METADATA_KEY_MEDIA_ID))
                                .setDescription(it.getString(METADATA_KEY_TITLE))
                                .build(), it.getLong(METADATA_KEY_FLAGS).toInt()
                        )
                    } else {
                        MediaBrowserCompat.MediaItem(
                            MediaDescriptionCompat.Builder()
                                .setTitle(it.getString(METADATA_KEY_TITLE))
                                .setMediaId(it.getString(METADATA_KEY_MEDIA_ID))
                                .setDescription(it.getString(METADATA_KEY_TITLE))
                                .setIconUri(it.getString(METADATA_KEY_ART_URI).toUri())
                                .build(), it.getLong(METADATA_KEY_FLAGS).toInt()
                        )
                    }
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

    private fun preparePlaylist(
        metadataList: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playWhenReady: Boolean,
        playbackStartPositionMs: Long
    ) {
        var initialWindowIndex = if (itemToPlay == null) 0 else metadataList.indexOf(itemToPlay)

        currentPlaylistItems = metadataList
//        exoPlayer.setMediaSource(musicSource.asMediaSource(dataSourceFactory))
//        exoPlayer.stop()
        exoPlayer.setMediaItems(
            metadataList.map { it.toMediaItem() }, initialWindowIndex, playbackStartPositionMs
        )
        exoPlayer.prepare()
        exoPlayer.playWhenReady = playWhenReady
    }

    inner class AudioPlayerEventListener : Player.Listener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING,
                Player.STATE_READY -> {
//                    notificationManager.showNotification(exoPlayer)
                    if (playbackState == Player.STATE_READY) {
                        //  saveRecentAudio() todo

                        if (!playWhenReady) {
                            stopForeground(false)
                            isForegroundService = false
                        }
                    }
                }
                else -> {
//                    notificationManager.hideNotification()
                }
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            Log.e("serviceTAG", error.message.orEmpty())
            Toast.makeText(
                applicationContext,
                error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private inner class AudioQueueNavigator : TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return musicSource.music[windowIndex].description
        }
    }

    private inner class MusicPlaybackPreparer : MediaSessionConnector.PlaybackPreparer {
        override fun onCommand(
            player: Player,
            command: String,
            extras: Bundle?,
            cb: ResultReceiver?
        ) = false

        override fun getSupportedPrepareActions(): Long {
            return PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
        }

        override fun onPrepare(playWhenReady: Boolean) = Unit

        override fun onPrepareFromMediaId(
            mediaId: String,
            playWhenReady: Boolean,
            extras: Bundle?
        ) {
            musicSource.whenReady {
                val itemToPlay: MediaMetadataCompat? = musicSource.music.find { item ->
                    item.getString(METADATA_KEY_MEDIA_ID) == mediaId
                }
                if (itemToPlay == null) {
                    Log.w("serviceTAG", "Content not found: MediaID=$mediaId")
                } else {

                    val playbackStartPositionMs = 0L

                    preparePlaylist(
                        buildPlaylist(itemToPlay),
                        itemToPlay,
                        playWhenReady,
                        playbackStartPositionMs
                    )
                }
            }
        }

        override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) =
            Unit

        override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit

        private fun buildPlaylist(item: MediaMetadataCompat): List<MediaMetadataCompat> =
            musicSource.filter {
                it.getString(METADATA_KEY_ALBUM) == item.getString(
                    METADATA_KEY_ALBUM
                )
            }
                .sortedBy { it.getLong(METADATA_KEY_TRACK_NUMBER).toInt() }
    }
}