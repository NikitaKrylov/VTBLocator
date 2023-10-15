package com.example.misisvtbhack.data

import com.yandex.mapkit.geometry.Point

data class Atm(
    val address: String,
    val allDay: Boolean,
    val latitude: Double,
    val longitude: Double,
    val services: Services
)
{
    val point: Point get() = Point(latitude, longitude)
}