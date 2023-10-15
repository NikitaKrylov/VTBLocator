package com.example.misisvtbhack.data

import com.yandex.mapkit.geometry.Point
import java.io.Serializable
import kotlin.math.pow
import kotlin.math.sqrt

data class Office(
    val address: String,
    val distance: Int,
    val hasRamp: String,
    val kep: Boolean,
    val latitude: Double,
    val longitude: Double,
    val metroStation: Any,
    val myBranch: Boolean,
    val officeType: String,
    val openHours: List<OpenHour>,
    val openHoursIndividual: List<OpenHoursIndividual>,
    val rko: String,
    val salePointFormat: String,
    val salePointName: String,
    val status: String,
    val suoAvailability: String
) : Serializable {
    fun distanceTo(point: Point) = sqrt( (latitude - point.latitude).pow(2) + (longitude - point.longitude).pow(2) )
    val point: Point get() = Point(latitude, longitude)
}