package com.coreman.navigator.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.widget.Toast
import kotlin.math.pow
import kotlin.math.sqrt


class SpeedService : Service(), SensorEventListener {

    val ACCELEROMETER_SPEED_ACTION = "accelerometer_speed_action"
    val ACCELEROMETER_SPEED = "accelerometes_speed"

    lateinit var sensorManager: SensorManager
    lateinit var sensorAccelerometer: Sensor

    var gravityX = 0f;
    var gravityY = 0f;
    var gravityZ = 0f;

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        Toast.makeText(this, "Service been created", Toast.LENGTH_LONG).show()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)

    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val alpha = 0.8f;
        val sensor = sensorEvent.sensor



        when(sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                var x = sensorEvent.values[0]
                var y = sensorEvent.values[1]
                var z = sensorEvent.values[2]

                gravityX = alpha * gravityX + (1 - alpha) * x
                gravityY = alpha * gravityY + (1 - alpha) * y
                gravityZ = alpha * gravityZ + (1 - alpha) * z

                x = sensorEvent.values[0] - gravityX
                y = sensorEvent.values[0] - gravityY
                z = sensorEvent.values[0] - gravityZ

                val speed = sqrt((x.pow(2f) + y.pow(2f) + z.pow(2f)).toDouble())


                Intent().also {
                    it.action = ACCELEROMETER_SPEED_ACTION
                    it.putExtra(ACCELEROMETER_SPEED, speed.toString())
                    sendBroadcast(it)
                }
            }
            Sensor.TYPE_GRAVITY -> {
                gravityX = sensorEvent.values[0]
                gravityY = sensorEvent.values[1]
                gravityZ = sensorEvent.values[2]
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // NOT IMPLEMENTED
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()

        Toast.makeText(this, "Service been destroyed", Toast.LENGTH_LONG).show()
    }
}