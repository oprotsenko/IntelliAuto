package com.automotive.bootcamp.mediaplayer.utils.serviceFinder

import android.content.Context
import android.service.notification.NotificationListenerService
import android.util.Log
import androidx.core.app.NotificationManagerCompat

/**
 * A notification listener service to allows us to grab active media sessions from their
 * notifications.
 * This class is only used on API 21+ because the Android media framework added getActiveSessions
 * in API 21.
 */
class NotificationListener : NotificationListenerService() {
    companion object {
        fun isEnable(context: Context): Boolean {
           return NotificationManagerCompat.getEnabledListenerPackages(context)
                .contains(context.packageName)
        }
    }
}