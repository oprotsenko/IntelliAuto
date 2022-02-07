package com.automotive.bootcamp.music_browse_service

import android.app.Notification
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_TITLE
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.automotive.bootcamp.music_browse_service.extensions.toMediaItem
import com.automotive.bootcamp.music_browse_service.sources.ServiceSources
import com.automotive.bootcamp.music_browse_service.utils.BROWSABLE_ROOT_ID
import com.automotive.bootcamp.music_browse_service.utils.METADATA_KEY_FLAGS
import com.automotive.bootcamp.music_browse_service.utils.NETWORK_ERROR
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyMusicService : MediaBrowserServiceCompat() {
    private lateinit var notificationManager: AudioNotificationManager

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var sessionConnector: MediaSessionConnector

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private val musicSource = ServiceSources(this)
    private val tree by lazy { BrowseTree(this, musicSource) }

    private var isForegroundService = false

    private var currentPlaylistItems: List<MediaMetadataCompat> = emptyList()
    private var currentMediaItemIndex: Int = 0

    private val serviceAudioAttributes = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    private val playerListener = PlayerEventListener()

    private val exoPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(this).build().apply {
            setAudioAttributes(serviceAudioAttributes, true)
            setHandleAudioBecomingNoisy(true)
            addListener(playerListener)
        }
    }

    private lateinit var currentPlayer: Player

    private val metadata = MediaMetadataCompat.Builder()
        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, "title").build()

    override fun onCreate() {
        super.onCreate()

        Log.d("serviceTAG", "onCreate")

        mediaSession = MediaSessionCompat(this, "MyMusicService")
        mediaSession.setMetadata(metadata)
        mediaSession.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )

        sessionToken = mediaSession.sessionToken

        notificationManager = AudioNotificationManager(
            this,
            mediaSession.sessionToken,
            PlayerNotificationListener()
        )

        sessionConnector = MediaSessionConnector(mediaSession)
        sessionConnector.setPlaybackPreparer(AudioPlaybackPreparer())
        sessionConnector.setQueueNavigator(AudioQueueNavigator(mediaSession))

        switchToPlayer(
            previousPlayer = null,
            newPlayer = exoPlayer
        )

        notificationManager.showNotificationForPlayer(currentPlayer)

        serviceScope.launch {
            musicSource.load()
        }
    }

    private fun switchToPlayer(previousPlayer: Player?, newPlayer: Player) {
        if (previousPlayer == newPlayer) {
            return
        }
        currentPlayer = newPlayer
        if (previousPlayer != null) {
            val playbackState = previousPlayer.playbackState
            if (currentPlaylistItems.isEmpty()) {
                currentPlayer.clearMediaItems()
                currentPlayer.stop()
            } else if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
                preparePlaylist(
                    metadataList = currentPlaylistItems,
                    itemToPlay = currentPlaylistItems[currentMediaItemIndex],
                    playWhenReady = previousPlayer.playWhenReady,
                    playbackStartPositionMs = previousPlayer.currentPosition
                )
            }
        }
        sessionConnector.setPlayer(newPlayer)
        previousPlayer?.stop(/* reset= */true)
    }

    private fun preparePlaylist(
        metadataList: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playWhenReady: Boolean,
        playbackStartPositionMs: Long
    ) {
        val initialWindowIndex = if (itemToPlay == null) 0 else metadataList.indexOf(itemToPlay)
        currentPlaylistItems = metadataList

        currentPlayer.playWhenReady = playWhenReady
        currentPlayer.stop()
        currentPlayer.setMediaItems(
            metadataList.map {
                it.toMediaItem()
            },
            initialWindowIndex, playbackStartPositionMs
        )
        currentPlayer.prepare()
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
    ): MediaBrowserServiceCompat.BrowserRoot {
        Log.d("serviceTAG", "onGetRoot")
        return MediaBrowserServiceCompat.BrowserRoot(BROWSABLE_ROOT_ID, null)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaItem>>) {
        Log.d("serviceTAG", "onLoadChildren " + parentId)
        val resultSent = musicSource.whenReady { initialized ->
            if (initialized) {
                Log.d("serviceTAG", "songs count " + tree[parentId]?.size)
                val treeItem = tree[parentId]?.map {
                    Log.d("serviceTAG", "children item " + it.getString(METADATA_KEY_TITLE))
                    MediaItem(
                        MediaDescriptionCompat.Builder()
                            .setTitle(it.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
                            .setMediaId(it.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID))
                            .setDescription(it.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
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

    private inner class AudioQueueNavigator(
        mediaSession: MediaSessionCompat
    ) : TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            if (windowIndex < currentPlaylistItems.size) {
                return currentPlaylistItems[windowIndex].description
            }
            return MediaDescriptionCompat.Builder().build()
        }
    }

    private inner class AudioPlaybackPreparer : MediaSessionConnector.PlaybackPreparer {
        override fun getSupportedPrepareActions(): Long =
            PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH or
                    PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH

        override fun onPrepare(playWhenReady: Boolean) {
//            val recentSong = storage.loadRecentSong() ?: return
//            onPrepareFromMediaId(
//                recentSong.mediaId!!,
//                playWhenReady,
//                recentSong.description.extras
//            )
        }

        override fun onPrepareFromMediaId(
            mediaId: String,
            playWhenReady: Boolean,
            extras: Bundle?
        ) {
//            mediaSource.whenReady {
//                val itemToPlay: MediaMetadataCompat? = mediaSource.find { item ->
//                    item.id == mediaId
//                }
//                if (itemToPlay == null) {
//                    Log.w(TAG, "Content not found: MediaID=$mediaId")
//                    // TODO: Notify caller of the error.
//                } else {
//
//                    val playbackStartPositionMs =
//                        extras?.getLong(
//                            MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS,
//                            C.TIME_UNSET
//                        )
//                            ?: C.TIME_UNSET
//
//                    preparePlaylist(
//                        buildPlaylist(itemToPlay),
//                        itemToPlay,
//                        playWhenReady,
//                        playbackStartPositionMs
//                    )
//                }
//            }
        }

        override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) =
            Unit

        override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit

        override fun onCommand(
            player: Player,
            command: String,
            extras: Bundle?,
            cb: ResultReceiver?
        ) = false

//        private fun buildPlaylist(item: MediaMetadataCompat): List<MediaMetadataCompat> =
//            mediaSource.filter { it.album == item.album }.sortedBy { it.trackNumber }
    }

    private inner class PlayerNotificationListener :
        PlayerNotificationManager.NotificationListener {
        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            if (ongoing && !isForegroundService) {
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(applicationContext, this@MyMusicService.javaClass)
                )

                startForeground(notificationId, notification)
                isForegroundService = true
            }
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        }
    }

    private inner class PlayerEventListener : Player.Listener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING,
                Player.STATE_READY -> {
                    notificationManager.showNotificationForPlayer(currentPlayer)
                    if (playbackState == Player.STATE_READY) {

                        // When playing/paused save the current media item in persistent
                        // storage so that playback can be resumed between device reboots.
                        // Search for "media resumption" for more information.

                        // saveRecentSongToStorage()

                        if (!playWhenReady) {
                            stopForeground(false)
                            isForegroundService = false
                        }
                    }
                }
                else -> {
                    notificationManager.hideNotification()
                }
            }
        }

        override fun onEvents(player: Player, events: Player.Events) {
            if (events.contains(Player.EVENT_POSITION_DISCONTINUITY)
                || events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)
                || events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)
            ) {
                currentMediaItemIndex = if (currentPlaylistItems.isNotEmpty()) {
                    Util.constrainValue(
                        player.currentMediaItemIndex,
                        /* min= */ 0,
                        /* max= */ currentPlaylistItems.size - 1
                    )
                } else 0
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            var message = R.string.generic_error;
            Log.e("serviceTAG", "Player error: " + error.errorCodeName + " (" + error.errorCode + ")");
            if (error.errorCode == PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS
                || error.errorCode == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND
            ) {
                message = R.string.error_media_not_found;
            }
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        }
    }
}