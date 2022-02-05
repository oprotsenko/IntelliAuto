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
    private val defaultDataSource: DefaultDataSource.Factory by inject()

    private val browseTree: BrowseTree by lazy {
        BrowseTree(applicationContext, musicSource)
    }

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private var currentAudio: MediaMetadataCompat? = null

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var musicNotificationManager: MusicNotificationManager
    private lateinit var musicPlayerEventListener: MusicPlayerEventListener

    var isForegroundService = false

    private var currentPlayingAudio: MediaMetadataCompat? = null
    private var isPlayerInitialized = false

    companion object {
        var currentAudioDuration = 0L
            private set
    }

    private fun preparePlayer(
        audios: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playNow: Boolean
    ) {
        val currentAudioIndex =
            if (currentPlayingAudio == null && !audios.isNullOrEmpty()) 0 else audios.indexOf(
                itemToPlay
            )
        player.setMediaSource(musicSource.asMediaSource(defaultDataSource))
        player.prepare()
        player.seekTo(currentAudioIndex, 0L)
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
        browseTree.mediaIdToChildren.forEach {
            it.value.forEach { mediaMetadataCompat ->
                mediaSession.setMetadata(mediaMetadataCompat)
            }
        }
        sessionToken = mediaSession.sessionToken
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

        val musicPlaybackPreparer = MusicPlaybackPreparer(musicSource) {
            currentPlayingAudio = it
            preparePlayer(musicSource.audios, it, true)
        }

//        mediaSessionConnector = MediaSessionConnector(mediaSession)
//        mediaSessionConnector.setPlaybackPreparer(musicPlaybackPreparer)
//        mediaSessionConnector.setQueueNavigator(MusicQueueNavigator())
//        mediaSessionConnector.setPlayer(player)

//        musicPlayerEventListener = MusicPlayerEventListener(this)
//        player.addListener(musicPlayerEventListener)
//        musicNotificationManager.showNotification(player)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        player.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()

        player.removeListener(musicPlayerEventListener)
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
        val list = mutableListOf<MediaBrowserCompat.MediaItem>()
        val desc = MediaDescriptionCompat.Builder().setDescription("item desc").setTitle("media item").setMediaId("id")

        val item = MediaBrowserCompat.MediaItem(desc.build(), FLAG_PLAYABLE)
        list.add(item)
        result.sendResult(list)
//        serviceScope.launch {
//            musicSource.retrieveRemoteAudio()
//            val resultSent = musicSource.whenReady { isInitialized ->
//                if (isInitialized) {
//                    result.sendResult(musicSource.audios.map {
//                        MediaBrowserCompat.MediaItem(
//                            it.description,
//                            FLAG_PLAYABLE
//                        )
//                    }.toMutableList())
//                } else {
//                    mediaSession.sendSessionEvent(NETWORK_ERROR, null)
//                    result.sendResult(null)
//                }
//            }
//            if (!resultSent) {
//            result.detach()
//        }
//        }
//        val resultSent = musicSource.whenReady { isInitialized ->
//            if (isInitialized) {
//                val children = browseTree[parentId]?.map { item ->
//                    MediaBrowserCompat.MediaItem(
//                        item.description,
//                        item.getLong(METADATA_KEY_FLAGS).toInt()
//                    )
//                }?.toMutableList()
//                result.sendResult(children)
//                result.sendResult(musicSource.asMediaItems())
//                if (!isPlayerInitialized && musicSource.audios.isNotEmpty()) {
//                    preparePlayer(musicSource.audios, musicSource.audios[0], false)
//                    isPlayerInitialized = true
//                }
//            } else {
//                mediaSession.sendSessionEvent(NETWORK_ERROR, null)
//                result.sendResult(null)
//            }
//        }
//        if (!resultSent) {
//            result.detach()
//        }
    }

//    private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSession) {
//        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
//            return musicSource.audios[windowIndex].description
//        }
//    }
}