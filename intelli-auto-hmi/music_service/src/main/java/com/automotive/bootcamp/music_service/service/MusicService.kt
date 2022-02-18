package com.automotive.bootcamp.music_service.service

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import android.support.v4.media.RatingCompat
import android.support.v4.media.RatingCompat.RATING_HEART
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.media.MediaBrowserServiceCompat
import androidx.media.utils.MediaConstants.*
import com.automotive.bootcamp.music_service.data.ServiceSources
import com.automotive.bootcamp.music_service.data.models.AudioItem
import com.automotive.bootcamp.music_service.utils.*
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MusicService : MediaBrowserServiceCompat() {

    //    private lateinit var notificationManager: AudioNotificationManager
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var sessionConnector: MediaSessionConnector

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private val musicSource: ServiceSources by inject()
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

    private var curPlayingSong: MediaMetadataCompat? = null
    private var currentPlaylistItems: List<MediaMetadataCompat> = emptyList()
    private var currentMediaItemIndex: Int = 0
    private var currentRoot: String? = null

    var isForegroundService = false

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

        mediaSession.setCallback(object : MediaSessionCompat.Callback() {
            override fun onSetRating(rating: RatingCompat?) {
                saveFavouriteAudio()
            }
        })
        sessionToken = mediaSession.sessionToken
//        notificationManager = AudioNotificationManager(
//            this,
//            mediaSession.sessionToken,
//            MusicPlayerNotificationListener(this)
//        ) {
//            currentAudioDuration = exoPlayer.duration
//        }
        mediaSession.setRatingType(RATING_HEART)

        val playbackPreparer = MusicPlaybackPreparer {
            curPlayingSong = it
        }
        sessionConnector = MediaSessionConnector(mediaSession)
        sessionConnector.setPlayer(exoPlayer)
        sessionConnector.setPlaybackPreparer(playbackPreparer)
        sessionConnector.setQueueNavigator(AudioQueueNavigator())

//        sessionConnector.setRatingCallback(object : MediaSessionConnector.RatingCallback {
//            override fun onCommand(
//                player: Player,
//                command: String,
//                extras: Bundle?,
//                cb: ResultReceiver?
//            ): Boolean = false
//
//            override fun onSetRating(player: Player, rating: RatingCompat) {
////                Log.d("favourite", curPlayingSong?.getString(METADATA_KEY_MEDIA_ID).orEmpty())
//                saveFavouriteAudio()
//            }
//
//            override fun onSetRating(player: Player, rating: RatingCompat, extras: Bundle?) {
//                TODO("Not yet implemented")
//            }
//        })

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
            putInt(
                DESCRIPTION_EXTRAS_KEY_CONTENT_STYLE_PLAYABLE,
                DESCRIPTION_EXTRAS_VALUE_CONTENT_STYLE_GRID_ITEM
            )
        }
        return BrowserRoot(BROWSABLE_ROOT_ID, rootExtras)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        Log.d("serviceTAG", "onLoadChildren " + parentId)
        currentRoot = parentId
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

        exoPlayer.setMediaItems(
            metadataList.map { it.toMediaItem() }, initialWindowIndex, playbackStartPositionMs
        )
        exoPlayer.prepare()
        exoPlayer.playWhenReady = playWhenReady
    }

    private fun saveFavouriteAudio() {
        val item = currentPlaylistItems[exoPlayer.currentMediaItemIndex]
        Log.d("curSong", "favouriteSong" + item.getString(METADATA_KEY_TITLE))
        serviceScope.launch {
            musicSource.addToFavourite(
                item.getString(
                    METADATA_KEY_MEDIA_ID
                ).toLong()
            )
        }
        tree.addMetadata(FAVOURITE_ROOT_ID, item)
    }

    private fun saveRecentAudio() {
        val item = currentPlaylistItems[exoPlayer.currentMediaItemIndex]
        Log.d("curSong", "recentSong" + item.getString(METADATA_KEY_TITLE))
        serviceScope.launch {
            musicSource.addToRecent(
                item.getString(
                    METADATA_KEY_MEDIA_ID
                ).toLong()
            )
        }
        tree.addMetadata(RECENT_ROOT_ID, item)
    }

    inner class AudioPlayerEventListener : Player.Listener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING,
                Player.STATE_READY -> {
//                    notificationManager.showNotification(exoPlayer)
                    if (playbackState == Player.STATE_READY) {
                          saveRecentAudio()

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
            Toast.makeText(
                applicationContext,
                error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private inner class AudioQueueNavigator : TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
//            val currentList = musicSource.filter {
//                it.getString(METADATA_KEY_ALBUM) == currentRoot
//            }
//            return currentList[windowIndex].description
            return currentPlaylistItems[windowIndex].description
        }
    }

    private inner class MusicPlaybackPreparer(
        private val playerPrepared: (MediaMetadataCompat?) -> Unit
    ) : MediaSessionConnector.PlaybackPreparer {
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
                val itemToPlay: MediaMetadataCompat? = musicSource.sourceList.find { item ->
                    item.getString(METADATA_KEY_MEDIA_ID) == mediaId
                }
                if (itemToPlay == null) {
                    Toast.makeText(
                        applicationContext,
                        "No audio to play",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val playbackStartPositionMs = 0L
                    preparePlaylist(
                        buildPlaylist(itemToPlay),
                        itemToPlay,
                        playWhenReady,
                        playbackStartPositionMs
                    )
                    playerPrepared(itemToPlay)
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
            }.sortedBy { it.getLong(METADATA_KEY_TRACK_NUMBER).toInt() }
    }
}