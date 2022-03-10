package com.automotive.bootcamp.launcher.presentation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.automotive.bootcamp.common.utils.SENSOR_ACCURACY
import com.automotive.bootcamp.launcher.R
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MainActivity : AppCompatActivity(R.layout.activity_main), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    lateinit var sensor: Sensor

    private val job = Job()
    private val activityScope = CoroutineScope(Dispatchers.Main + job)

    var eventStartTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
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
            eventStartTime = System.currentTimeMillis()
            activityScope.launch {
                delay(2000)
                Log.d("eventStarted", "$eventStartTime, current time ${System.currentTimeMillis()}")
                if (event != null && event.values[0] > SENSOR_ACCURACY) {
                    Log.d("lightEventStarted", "${System.currentTimeMillis()} day mode turned on")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                } else {
                    Log.d("nightEventStarted", "${System.currentTimeMillis()} night mode turned on")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("onAccuracyChanged", accuracy.toString())
    }

    companion object {
        var isActiveLightSensor = true
    }

}