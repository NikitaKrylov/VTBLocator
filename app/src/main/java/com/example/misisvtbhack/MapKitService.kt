package com.example.misisvtbhack
import com.yandex.mapkit.map.Map


import android.content.Context
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition

class MapKitService(context: Context, val map: Map) {

    init {
        MapKitFactory.initialize(context)
    }
    fun moveCamera(point: Point){
        map.move(
            CameraPosition(point, 13f, 0f, 0f),
            Animation(Animation.Type.LINEAR, 1f),
            null
        )
    }
    fun zoom(updatedValue: Float){
        map.move(
            CameraPosition(
                map.cameraPosition.target,
                map.cameraPosition.zoom+updatedValue,
                0f,
                0f
            ),
            Animation(Animation.Type.LINEAR, 0.25f),
            null
        )
    }


}