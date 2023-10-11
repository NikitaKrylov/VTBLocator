package com.example.misisvtbhack.components
import com.yandex.mapkit.map.Map


import android.content.Context
import android.widget.Toast
import com.example.misisvtbhack.R
import com.example.misisvtbhack.data.BankBranch
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.directions.driving.VehicleType
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.geo.PolylineIndex
import com.yandex.mapkit.geometry.geo.PolylineUtils
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PolylineMapObject


class MapKitService(val context: Context, val map: Map) {

    val bankPinsCollection = map.mapObjects.addCollection()
    val drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
    val routesCollection = map.mapObjects.addCollection()

    val drivingRouteListener = object : DrivingSession.DrivingRouteListener {
        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
            drivingRoutes.forEachIndexed {index, route ->
                routesCollection.addPolyline(route.geometry).apply {
                    if (index == 0) styleMainRoute() else styleAlternativeRoute()
                }
            }

        }

        override fun onDrivingRoutesError(p0: com.yandex.runtime.Error) {
            TODO("Not yet implemented")
        }

    }
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

    fun makePoints(branches: List<BankBranch>){
        branches.forEach {
            makePoint(it.point)
        }
    }
    fun makePoint(point: Point){
        bankPinsCollection.addPlacemark(point)
    }

    fun buildRoute(from: Point, to: Point, routes : Int = 2){
        val drivingOptions = DrivingOptions().apply {
            routesCount = routes // количество похожих маршрутов
        }
        val vehicleOptions = VehicleOptions().apply {
            vehicleType = VehicleType.DEFAULT
        }

        val points = buildList {
            add(RequestPoint(from, RequestPointType.WAYPOINT, null, null))
            add(RequestPoint(to, RequestPointType.WAYPOINT, null, null))
        }
        val drivingSession = drivingRouter.requestRoutes(
            points,
            drivingOptions,
            vehicleOptions,
            drivingRouteListener
        )
    }

    private fun PolylineMapObject.styleMainRoute(){
        zIndex = 10f
        setStrokeColor(context.getColor(android.R.color.darker_gray))
        strokeWidth = 5f
        outlineColor = context.getColor(android.R.color.black)
        outlineWidth = 3f
    }

    private fun PolylineMapObject.styleAlternativeRoute() {
        zIndex = 5f
        setStrokeColor(context.getColor(android.R.color.holo_blue_dark))
        strokeWidth = 4f
        outlineColor = context.getColor(android.R.color.holo_green_dark)
        outlineWidth = 2f
    }

    fun distanceBetweenPointsOnRoute(route: DrivingRoute, first: Point, second: Point): Float {
        val polylineIndex = PolylineUtils.createPolylineIndex(route.geometry)
        val firstPosition = polylineIndex.closestPolylinePosition(first, PolylineIndex.Priority.CLOSEST_TO_RAW_POINT, 1.0)!!
        val secondPosition = polylineIndex.closestPolylinePosition(second, PolylineIndex.Priority.CLOSEST_TO_RAW_POINT, 1.0)!!
        return PolylineUtils.distanceBetweenPolylinePositions(route.geometry, firstPosition, secondPosition)
    }

    fun timeTravelToPoint(route: DrivingRoute, point: Point): Float {
        val currentPosition = route.routePosition
        val distance = distanceBetweenPointsOnRoute(route, currentPosition.point, point)
        val targetPosition = currentPosition.advance(distance.toDouble())
        return (targetPosition.timeToFinish() - currentPosition.timeToFinish()).toFloat()
    }

}