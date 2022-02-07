package com.automotive.bootcamp.music_browse_service

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.media.MediaBrowserServiceCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.automotive.bootcamp.music_browse_service.data.ResourcesAudioSource
import com.automotive.bootcamp.music_browse_service.data.RetrofitAudioSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.get

/**
 * This class provides a MediaBrowser through a service. It exposes the media library to a browsing
 * client, through the onGetRoot and onLoadChildren methods. It also creates a MediaSession and
 * exposes it through its MediaSession.Token, which allows the client to create a MediaController
 * that connects to and send control commands to the MediaSession remotely. This is useful for
 * user interfaces that need to interact with your media session, like Android Auto. You can
 * (should) also use the same service from your app"s UI, which gives a seamless playback
 * experience to the user.
 *
 *
 * To implement a MediaBrowserService, you need to:
 *
 *  *  Extend [MediaBrowserServiceCompat], implementing the media browsing
 * related methods [MediaBrowserServiceCompat.onGetRoot] and
 * [MediaBrowserServiceCompat.onLoadChildren];
 *
 *  *  In onCreate, start a new [MediaSessionCompat] and notify its parent
 * with the session"s token [MediaBrowserServiceCompat.setSessionToken];
 *
 *  *  Set a callback on the [MediaSessionCompat.setCallback].
 * The callback will receive all the user"s actions, like play, pause, etc;
 *
 *  *  Handle all the actual music playing using any method your app prefers (for example,
 * [android.media.MediaPlayer])
 *
 *  *  Update playbackState, "now playing" metadata and queue, using MediaSession proper methods
 * [MediaSessionCompat.setPlaybackState]
 * [MediaSessionCompat.setMetadata] and
 * [MediaSessionCompat.setQueue])
 *
 *  *  Declare and export the service in AndroidManifest with an intent receiver for the action
 * android.media.browse.MediaBrowserService
 *
 * To make your app compatible with Android Auto, you also need to:
 *
 *  *  Declare a meta-data tag in AndroidManifest.xml linking to a xml resource
 * with a &lt;automotiveApp&gt; root element. For a media app, this must include
 * an &lt;uses name="media"/&gt; element as a child.
 * For example, in AndroidManifest.xml:
 * &lt;meta-data android:name="com.google.android.gms.car.application"
 * android:resource="@xml/automotive_app_desc"/&gt;
 * And in res/values/automotive_app_desc.xml:
 * &lt;automotiveApp&gt;
 * &lt;uses name="media"/&gt;
 * &lt;/automotiveApp&gt;
 *
 */
class MyMusicService : MediaBrowserServiceCompat() {
    private lateinit var session: MediaSessionCompat
    private val resourcesAudioSource: ResourcesAudioSource = get()
    private val remoteAudioSource: RetrofitAudioSource = get()
    private val audioSources = mutableMapOf<String, AbstractAudioSource>()

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    private val tree by lazy { BrowseTree(this, musicSource) }

    private val callback = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
            val playbackState = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY)
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1f)
                .build()
            session.setPlaybackState(playbackState)
        }

//        override fun onSkipToQueueItem(queueId: Long) {}

//        override fun onSeekTo(position: Long) {}

//        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {}

        override fun onPause() {
            val playbackState = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PAUSE)
                .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1f)
                .build()
            session.setPlaybackState(playbackState)
        }

//        override fun onStop() {}

        override fun onSkipToNext() {
            val playbackState = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_SKIP_TO_NEXT)
                .setState(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT, 0, 1f)
                .build()
            session.setPlaybackState(playbackState)
        }

        override fun onSkipToPrevious() {
            val playbackState = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .setState(PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS, 0, 1f)
                .build()
            session.setPlaybackState(playbackState)
        }

//        override fun onCustomAction(action: String?, extras: Bundle?) {}

//        override fun onPlayFromSearch(query: String?, extras: Bundle?) {}
    }

    private val metadata = MediaMetadataCompat.Builder()
        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, "title").build()

    override fun onCreate() {
        super.onCreate()

        audioSources[LOCAL_ROOT_ID] = resourcesAudioSource
        audioSources[REMOTE_ROOT_ID] = remoteAudioSource

        session = MediaSessionCompat(this, "MyMusicService")
        sessionToken = session.sessionToken
        session.setCallback(callback)
        session.setMetadata(metadata)
        session.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )

        val playbackState = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY_PAUSE
                        or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            )
            .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1f)
            .build()
        session.setPlaybackState(playbackState)
    }

    override fun onDestroy() {
        session.release()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): MediaBrowserServiceCompat.BrowserRoot {
        return MediaBrowserServiceCompat.BrowserRoot("root", null)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaItem>>) {
        val list = mutableListOf<MediaBrowserCompat.MediaItem>()

        if (parentId == "root") {
            val treeItem = tree.mediaIdToChildren.map { item ->
                item.value.map {
                    MediaItem(
                        MediaDescriptionCompat.Builder()
                            .setTitle(it.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
                            .setMediaId(it.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID))
                            .setDescription(it.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
                            .build(), it.getLong(METADATA_KEY_FLAGS).toInt()
                    )
                }
            }
            treeItem.forEach {
                it.forEach { it1 ->
                    list.add(it1)
                }
            }
        } else {
            val mediaSource =  audioSources[parentId]
            mediaSource?.load()

            val resultsSent = mediaSource?.whenReady { successfullyInitialized ->
                if (successfullyInitialized) {
                    list = tree[parentId]?.map { item ->
                        MediaItem(item.description, item.flag)
                    }
                    result.sendResult(list)
                } else {
                    session.sendSessionEvent(NETWORK_ERROR, null)
                    result.sendResult(null)
                }
            }

            resultsSent?.let {
                if (!resultsSent) {
                    result.detach()
                }
            }
        }

        result.sendResult(list)


        when (parentId) {
            LOCAL_ROOT_ID -> {
                val res = musicSource.retrieveLocalAudio()
                val mediaItems = res.map { audioItem ->
                    val desc = MediaDescriptionCompat.Builder()
                        .setDescription(audioItem.artist)
                        .setTitle(audioItem.title)
                        .setMediaId(audioItem.id.toString())
                        .setMediaUri(Uri.parse(audioItem.url))
                        .setIconUri(Uri.parse(audioItem.cover))
                    MediaItem(desc.build(), MediaItem.FLAG_PLAYABLE)
                }
                mediaItems.forEach {
                    list.add(it)
                }
            }

            REMOTE_ROOT_ID -> {
                val res = musicSource.retrieveRemoteAudio()
                val mediaItems = res.map { audioItem ->
                    val desc = MediaDescriptionCompat.Builder()
                        .setDescription(audioItem.artist)
                        .setTitle(audioItem.title)
                        .setMediaId(audioItem.id.toString())
                        .setMediaUri(Uri.parse(audioItem.url))
                        .setIconUri(Uri.parse(audioItem.cover))
                    MediaItem(desc.build(), MediaItem.FLAG_PLAYABLE)
                }
                mediaItems.forEach {
                    list.add(it)
                }
            }
        }
    }
}