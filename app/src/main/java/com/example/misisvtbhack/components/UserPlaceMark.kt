package com.example.misisvtbhack.components

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.misisvtbhack.R
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.runtime.image.ImageProvider

class UserPlaceMark(context: Context, mapObjects: MapObjectCollection, intiPosition: Point, private val circleRadius: Float = 2f) {
    private val _placeMark = mapObjects.addPlacemark().apply {
        geometry = intiPosition
        setIcon(ImageProvider.fromBitmap(MapKitService.createBitmapFromVector(context, R.drawable.ic_user_pos)))
    }
    private val _circleMark = mapObjects.addCircle(Circle(intiPosition, circleRadius)).apply {
        fillColor = ContextCompat.getColor(context, android.R.color.holo_blue_bright)
        strokeColor = ContextCompat.getColor(context, android.R.color.holo_blue_light)
    }

    fun move(point: Point){
        _placeMark.geometry = point
        _circleMark.geometry = Circle(point, circleRadius)
    }
}