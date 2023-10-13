package com.example.misisvtbhack

import android.location.Location
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.misisvtbhack.data.Office
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.search.Response


enum class TargetType{
    OFFICE, ATMS
}

data class FilterData(
    val targetType: TargetType = TargetType.OFFICE
)

class MapViewModel : ViewModel() {
    val drivingRoutes = MutableLiveData<MutableList<DrivingRoute>>()
    val currentRoute = MutableLiveData<DrivingRoute>()
    val searchResponse = MutableLiveData<Response>()
    val currentLocation = MutableLiveData<Location>()
    val offices = MediatorLiveData<List<Office>>()

    val filterData = MutableLiveData<FilterData>()

}