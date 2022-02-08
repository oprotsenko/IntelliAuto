package com.automotive.bootcamp.music_browse_service.callbacks

import android.util.Log
import android.widget.Toast
import com.automotive.bootcamp.music_browse_service.MyMusicService
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player

class MusicPlayerEventListener(
    private val musicService: MyMusicService
) : Player.Listener {
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        Log.d("MyMusicService", playbackState.toString())

        if (playbackState == Player.STATE_READY) {
            Log.d("MyMusicService", playbackState.toString())
            if(!playWhenReady){
                musicService.stopForeground(false)
            }
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(musicService, "some error", Toast.LENGTH_SHORT).show()
    }
}

//private inner class PlayerEventListener : Player.Listener {
//    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//        Log.d("serviceTAG", "onPlayerStateChanged -> " + playbackState)
//
//        when (playbackState) {
//            Player.STATE_BUFFERING,
//            Player.STATE_READY -> {
//                notificationManager.showNotificationForPlayer(currentPlayer)
//                if (playbackState == Player.STATE_READY) {
//                    Log.d("serviceTAG", "STATE_READY ")
//
//                    // When playing/paused save the current media item in persistent
//                    // storage so that playback can be resumed between device reboots.
//                    // Search for "media resumption" for more information.
//
//                    // saveRecentSongToStorage()
//
//                    if (!playWhenReady) {
//                        stopForeground(false)
//                        isForegroundService = false
//                    }
//                }
//            }
//            else -> {
//                notificationManager.hideNotification()
//            }
//        }
//    }
//
//    override fun onEvents(player: Player, events: Player.Events) {
//        if (events.contains(Player.EVENT_POSITION_DISCONTINUITY)
//            || events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)
//            || events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)
//        ) {
//            currentMediaItemIndex = if (currentPlaylistItems.isNotEmpty()) {
//                Util.constrainValue(
//                    player.currentMediaItemIndex,
//                    /* min= */ 0,
//                    /* max= */ currentPlaylistItems.size - 1
//                )
//            } else 0
//        }
//    }
//
//    override fun onPlayerError(error: PlaybackException) {
//        var message = R.string.generic_error;
//        Log.e("serviceTAG", "Player error: " + error.errorCodeName + " (" + error.errorCode + ")");
//        if (error.errorCode == PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS
//            || error.errorCode == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND
//        ) {
//            message = R.string.error_media_not_found;
//        }
//        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
//    }
//}