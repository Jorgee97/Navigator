package com.coreman.navigator

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.coreman.navigator.services.SpeedService
import com.google.android.material.snackbar.Snackbar
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {

    private var permissionsManager : PermissionsManager = PermissionsManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Hide the key
        Mapbox.getInstance(this, getString(R.string.mapbox_instance_key))
        setContentView(R.layout.activity_main)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        Intent(this, SpeedService::class.java).also {intent ->
            startService(intent)
        }

        /*
        IntentFilter().also {
            it.addAction(SpeedService().ACCELEROMETER_SPEED_ACTION)
            registerReceiver(mReceiver, it)
        }
        */
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS)

        initializeLocationService()
    }

    private fun initializeLocationService() {
        if (!PermissionsManager.areLocationPermissionsGranted(this)) {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.hasExtra(SpeedService().ACCELEROMETER_SPEED)) {
                // TODO: Finish the implementation of the speed
            }
        }
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            initializeLocationService()
        } else {
            Snackbar.make(mapView, R.string.location_not_allowed, Snackbar.LENGTH_LONG).setAction(
                R.string.ok
            ) {
                finish()
            }.show()
        }
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Snackbar.make(mapView, R.string.current_location_explanation, Snackbar.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
