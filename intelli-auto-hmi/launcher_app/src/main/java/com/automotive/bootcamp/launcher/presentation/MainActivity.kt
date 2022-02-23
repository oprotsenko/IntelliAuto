package com.automotive.bootcamp.launcher.presentation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.media.MediaBrowserServiceCompat
import com.automotive.bootcamp.common.utils.SENSOR_ACCURACY
import com.automotive.bootcamp.launcher.R
import com.automotive.bootcamp.mediaplayer.utils.serviceFinder.MediaAppDetails
import java.util.ArrayList

class MainActivity : AppCompatActivity(R.layout.activity_main), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    lateinit var sensor: Sensor

    var currentAccuracy: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        mediaApps.map {
            Log.d("serviceTAGA", it.appName)
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (isActiveLightSensor) {
            if (event != null && event.values[0] > SENSOR_ACCURACY) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        currentAccuracy = accuracy
        Log.d("accuracy", accuracy.toString())
    }

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

    companion object {
        var isActiveLightSensor = true
    }

}