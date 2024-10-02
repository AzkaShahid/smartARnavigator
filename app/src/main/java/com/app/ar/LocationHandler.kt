package com.app.ar

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

fun interface LocationListener {
    companion object {
        private val locationListeners = mutableListOf<LocationListener>()

        fun register(locationListener: LocationListener) {
            if (locationListeners.all { it != locationListener }) {
                locationListeners.add(locationListener)
            }
        }

        fun unregister(locationListener: LocationListener) {
            locationListeners.remove(locationListener)
        }

        fun trigger(location: Location) {
            locationListeners.forEach {
                it.onLocationChanged(location)
            }
        }
    }

    fun onLocationChanged(location: Location)
}

class LocationHandler constructor(private val activity: Activity) {
    companion object {
        const val LOCATION_REQUEST_CODE_PERMISSIONS = 0
        val LOCATION_REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(activity)

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult?.locations?.forEach {
                LocationListener.trigger(it)
            }
        }
    }

    private fun checkPermission(type: String): Boolean {
        val permission = ActivityCompat.checkSelfPermission(activity, type)
        return permission == PackageManager.PERMISSION_GRANTED
    }

    fun start() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProvider.lastLocation.addOnSuccessListener { location ->
            location?.let {
                LocationListener.trigger(it)
            }
        }

        val locationRequest = LocationRequest.create()?.apply {
            interval = 100
            fastestInterval = 50
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if(locationRequest != null){
            fusedLocationProvider.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        }
    }

    fun stop() {
        fusedLocationProvider.removeLocationUpdates(locationCallback)
    }
}