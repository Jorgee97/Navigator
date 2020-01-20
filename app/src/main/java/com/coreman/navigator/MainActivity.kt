package com.coreman.navigator

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.coreman.navigator.services.LocationService
import com.coreman.navigator.utils.SpeedConverter
import com.google.android.material.snackbar.Snackbar
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback, Style.OnStyleLoaded, PermissionsListener {

    private var permissionsManager : PermissionsManager = PermissionsManager(this)
    private lateinit var map: MapboxMap
    private lateinit var symbolManager: SymbolManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Hide the key
        Mapbox.getInstance(this, getString(R.string.mapbox_instance_key))
        setContentView(R.layout.activity_main)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)


        IntentFilter().also {
            it.addAction(LocationService().LOCATION_UPDATE_ACTION)
            registerReceiver(mReceiver, it)
        }

    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap
        mapboxMap.setStyle("mapbox://styles/thecoreman27/ck5l8sinp0moz1io3vslo8emg") {
            initializeLocationService(it)
        }
    }

    private fun initializeLocationService(style: Style) {
        if (!PermissionsManager.areLocationPermissionsGranted(this)) {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        } else {
            symbolManager = SymbolManager(mapView, map, style)

            symbolManager.iconAllowOverlap = true
            symbolManager.iconIgnorePlacement = true


            symbolManager.create(SymbolOptions()
                .withLatLng(LatLng(4.667426, -74.056624))
                .withIconImage("trip_origin-24px")
                .withTextField("Mi Aguila")
                .withIconSize(2.0f))


            symbolManager.create(SymbolOptions()
                .withLatLng(LatLng(4.672655, -74.054071))
                .withIconImage("pin_drop")
                .withTextField("Virrey Park Hotel")
                .withIconSize(2.0f))

            Intent(this, LocationService::class.java).also {
                startService(it)
            }
        }
    }

    override fun onStyleLoaded(style: Style) {
        initializeLocationService(style)
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.hasExtra(LocationService().DEVICE_SPEED)) {
                val actualSpeed = intent.getStringExtra(LocationService().DEVICE_SPEED)

                val speedInKmPerHour =
                    SpeedConverter().calculateSpeedInKilometers(actualSpeed!!.toDouble())

                txt_speed.text = "%.2f".format(speedInKmPerHour)
            }

            if(intent.hasExtra(LocationService().LOCATION_UPDATE)) {
                val currentLocation = intent.getStringExtra(LocationService().LOCATION_UPDATE)!!.split(',')
                val latLng = LatLng(currentLocation[0].toDouble(), currentLocation[1].toDouble())
                map.cameraPosition = CameraPosition.Builder()
                    .target(latLng)
                    .zoom(15.0)
                    .build()

                symbolManager.create(SymbolOptions()
                    .withLatLng(latLng)
                    .withIconImage("dot-9")
                    .withIconSize(2.0f))
            }
        }
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            map.getStyle(this)
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
