package com.automotive.bootcamp.mediaplayer.utils.serviceFinder

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.media.MediaBrowserServiceCompat

class FindMediaBrowserAppsTask(
    context: Context,
    callback: AppListUpdatedCallback,
) : FindMediaAppsTask(callback, sortAlphabetical = true) {

    private val packageManager: PackageManager = context.packageManager
    private val resources: Resources = context.resources

    /**
     * Finds installed packages that have registered a
     * [android.service.media.MediaBrowserService] or
     * [android.support.v4.media.MediaBrowserServiceCompat] service by
     * looking for packages that have services that respond to the
     * "android.media.browse.MediaBrowserService" action.
     */
    override val mediaApps: List<MediaAppDetails>
        get() {
            val mediaApps = ArrayList<MediaAppDetails>()
            val mediaBrowserIntent = Intent(MediaBrowserServiceCompat.SERVICE_INTERFACE)

            // Build an Intent that only has the MediaBrowserService action and query
            // the PackageManager for apps that have services registered that can
            // receive it.
            val services = packageManager.queryIntentServices(
                mediaBrowserIntent,
                PackageManager.GET_RESOLVED_FILTER
            )

            if (services != null && !services.isEmpty()) {
                for (info in services) {
                    mediaApps.add(
                        MediaAppDetails(
                            info.serviceInfo, packageManager, resources
                        )
                    )
                }
            }
            return mediaApps
        }
}