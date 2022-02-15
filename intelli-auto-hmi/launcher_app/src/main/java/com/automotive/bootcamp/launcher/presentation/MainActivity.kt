package com.automotive.bootcamp.launcher.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.media.MediaBrowserServiceCompat
import com.automotive.bootcamp.launcher.R
import com.automotive.bootcamp.mediaplayer.utils.serviceFinder.MediaAppDetails
import java.util.ArrayList

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    /**
     * Finds installed packages that have registered a
     * [android.service.media.MediaBrowserService] or
     * [android.support.v4.media.MediaBrowserServiceCompat] service by
     * looking for packages that have services that respond to the
     * "android.media.browse.MediaBrowserService" action.
     */
    val mediaApps: List<MediaAppDetails>
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
                        MediaAppDetails(info.serviceInfo, packageManager, resources)
                    )
                }
            }
            return mediaApps
        }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        mediaApps.map {
            Log.d("serviceTAGA", it.appName)
        }
    }
}