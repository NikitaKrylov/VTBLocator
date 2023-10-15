package com.example.misisvtbhack

import com.example.misisvtbhack.data.Office
import com.yandex.mapkit.geometry.Point

class BranchSorter {
    fun sortOffices(point: Point, offices: List<Office>): List<Office> =
        offices.sortedByDescending {it.distanceTo(point)}.reversed()
}