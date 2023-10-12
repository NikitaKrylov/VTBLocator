package com.example.misisvtbhack.components

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.example.misisvtbhack.MapViewModel
import com.yandex.mapkit.geometry.Point

class LocationService(private val activity: Activity, private val viewModel: MapViewModel) {
    val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
    fun setLocationUpdateListener(){
        checkLocationPermissions(activity)

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 10f
        ) {
            val point = Point(it.latitude, it.longitude)
            viewModel.currentLocation.postValue(it)
        }
    }


    companion object {
        fun checkLocationPermissions(activity: Activity) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    100
                )
            }
        }
    }
}