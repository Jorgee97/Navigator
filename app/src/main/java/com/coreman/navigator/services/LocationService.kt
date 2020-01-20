package com.coreman.navigator.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.widget.Toast

class LocationService : Service(), LocationListener {

    lateinit var locationManager: LocationManager

    // Request updates every 3 seconds and 15 meters of difference.
    val LOCATION_INTERVAL : Long = 3000
    val LOCATION_DISTANCE = 15f

    var LOCATION_UPDATE_ACTION = "location update action"
    var LOCATION_UPDATE = "location update"
    var DEVICE_SPEED = "device speed"

    override fun onLocationChanged(location: Location) {
        Intent().also {
            it.action = LocationService().LOCATION_UPDATE_ACTION
            it.putExtra(LocationService().LOCATION_UPDATE, location.latitude.toString() + "," + location.longitude.toString())
            it.putExtra(LocationService().DEVICE_SPEED, location.speed.toString())

            sendBroadcast(it)
        }
    }

    override fun onProviderDisabled(provider: String?) {
        Toast.makeText(this, "GPS needs to be enabled for this application to work.", Toast.LENGTH_LONG).show()
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY;
    }

    // I suppress the warning because I know I will ask for location permissions when the app opens,
    // and before the app start this service.
    @SuppressWarnings("ResourceType")
    override fun onCreate() {
        super.onCreate()
        initializeLocationManager()

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL,
            LOCATION_DISTANCE, this)

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL,
            LOCATION_DISTANCE, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::locationManager.isInitialized) {
            locationManager.removeUpdates(this)
        }
    }

    private fun initializeLocationManager() {
        if (!::locationManager.isInitialized) {
            locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // Setting accuracy to high
            Criteria().also {
                it.accuracy = Criteria.ACCURACY_FINE

                locationManager.getBestProvider(it, true)
            }
        }
    }
}