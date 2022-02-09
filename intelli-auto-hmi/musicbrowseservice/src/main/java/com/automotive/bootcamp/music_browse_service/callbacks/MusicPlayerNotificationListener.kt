package com.automotive.bootcamp.music_browse_service.callbacks

import android.app.Notification
import android.content.Intent
import androidx.core.content.ContextCompat
import com.automotive.bootcamp.music_browse_service.MyMusicService
import com.automotive.bootcamp.music_browse_service.utils.NOTIFICATION_ID
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class MusicPlayerNotificationListener(
    private val musicService: MyMusicService
) : PlayerNotificationManager.NotificationListener {
    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        musicService.apply {
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        }
    }

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)
        musicService.apply {
            if (ongoing && !isForegroundService) {
                ContextCompat.startForegroundService(this, Intent(applicationContext, this::class.java))
                startForeground(NOTIFICATION_ID, notification)
                isForegroundService = true
            }
        }
    }
}

//private inner class PlayerNotificationListener :
//    PlayerNotificationManager.NotificationListener {
//    override fun onNotificationPosted(
//        notificationId: Int,
//        notification: Notification,
//        ongoing: Boolean
//    ) {
//        if (ongoing && !isForegroundService) {
//            ContextCompat.startForegroundService(
//                applicationContext,
//                Intent(applicationContext, this@MyMusicService.javaClass)
//            )
//
//            startForeground(notificationId, notification)
//            isForegroundService = true
//        }
//    }
//
//    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
//        stopForeground(true)
//        isForegroundService = false
//        stopSelf()
//    }
//}