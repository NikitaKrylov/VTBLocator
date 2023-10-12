package com.example.misisvtbhack.data

import com.yandex.mapkit.directions.driving.DrivingSummarySession
import com.yandex.mapkit.directions.driving.Summary
import com.yandex.mapkit.geometry.Point
import com.yandex.runtime.Error


data class BankBranch(
    val point: Point,
    val address: String = "Some address",
    var currentDistance: String = "No info",
    var travelTime: String = "No info",
    var workload: Float = 0f,

//    var workload: Float
){
//    val _drivingSummaryListener = object : DrivingSummarySession.DrivingSummaryListener {
//        override fun onDrivingSummaries(summaries: MutableList<Summary>) {
//            if (summaries.isEmpty()) return
//            val summary = summaries.first()
//            travelTime = summary.weight.time.text
//            currentDistance = summary.weight.distance.text
//        }
//
//        override fun onDrivingSummariesError(p0: Error) {
//            TODO("Not yet implemented")
//        }
//
//    }
}
