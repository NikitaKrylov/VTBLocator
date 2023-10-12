package com.example.misisvtbhack

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.misisvtbhack.data.BankBranch
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.Response


class MapViewModel : ViewModel() {
    val drivingRoutes = MutableLiveData<MutableList<DrivingRoute>>()
    val currentRoute = MutableLiveData<DrivingRoute>()
    val searchResponse = MutableLiveData<Response>()
    val currentLocation = MutableLiveData<Location>()

    val bankBranches = MediatorLiveData<List<BankBranch>>(listOf(
        BankBranch(Point(55.605670, 37.534763)),
        BankBranch(Point(55.656633, 37.621223)),
        BankBranch(Point(55.656633, 37.611223)),
        BankBranch(Point(55.762919, 37.622122))
    ))



}