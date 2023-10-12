package com.example.misisvtbhack.components


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.misisvtbhack.MapViewModel
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
import com.yandex.mapkit.directions.driving.DrivingSession.DrivingRouteListener
import com.yandex.mapkit.directions.driving.DrivingSummarySession
import com.yandex.mapkit.directions.driving.DrivingSummarySession.DrivingSummaryListener
import com.yandex.mapkit.directions.driving.Summary
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.directions.driving.VehicleType
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.geo.PolylineIndex
import com.yandex.mapkit.geometry.geo.PolylineUtils
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.search.Session.SearchListener
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider


class MapKitService(val context: Context, val map: Map, val viewModel: MapViewModel) {

    private val bankPinsCollection = map.mapObjects.addCollection()
    private val drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
    private val routesCollection = map.mapObjects.addCollection()
    private val searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)

    private var _drivingSession: DrivingSession? = null
    private var _searchSession: Session? = null

    private val _drivingRouteListener = object : DrivingRouteListener {
        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
            drivingRoutes.forEachIndexed {index, route ->
                routesCollection.addPolyline(route.geometry).apply {
                    if (index == 0) {
                        styleMainRoute()
                        viewModel.currentRoute.postValue(route)
                    } else {
                        styleAlternativeRoute()
                    }
                }
                viewModel.drivingRoutes.postValue(drivingRoutes)

            }

        }

        override fun onDrivingRoutesError(error: com.yandex.runtime.Error) {
            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    private val searchListener = object : SearchListener{
        override fun onSearchResponse(response: Response) {
            viewModel.searchResponse.postValue(response)
        }

        override fun onSearchError(error: Error) {
            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
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

    fun moveToCurrentPosition(){
        viewModel.currentLocation.value?.let {
            map.move(
                CameraPosition(
                    Point(it.latitude, it.longitude),
                    12f,
                    0f,
                    0f
                ),
                Animation(Animation.Type.LINEAR, 0.5f),
                null
            )
        }

    }

    fun makePoints(branches: List<BankBranch>){
        branches.forEach {
            makePoint(it.point)
        }

    }
    fun makePoint(point: Point){
        val placemark = bankPinsCollection.addPlacemark().apply {
            geometry = point
            setIcon(ImageProvider.fromBitmap(createBitmapFromVector(R.drawable.placemark)))
        }
    }

    fun lazyBuildRoute(from: Point, to: Point, routes : Int = 2){
        routesCollection.clear()
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


        _drivingSession = drivingRouter.requestRoutes(
            points,
            drivingOptions,
            vehicleOptions,
            _drivingRouteListener
        )
        boundZoom(from, to)
    }

    fun lazyBuildRoute(from: Point, to: Point, _listener: DrivingSummaryListener){

        drivingRouter.requestRoutesSummary(
            buildList {
                add(RequestPoint(from, RequestPointType.WAYPOINT, null, null))
                add(RequestPoint(to, RequestPointType.WAYPOINT, null, null))
            },
            DrivingOptions().apply {
                routesCount = 1
            },
            VehicleOptions().apply {
                vehicleType = VehicleType.DEFAULT
            },
            _listener
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

    fun search(text: String, pageSize: Int = 32){
        val searchOptions = SearchOptions().apply {
            resultPageSize = 32
        }
        _searchSession = searchManager.submit(
            text,
            VisibleRegionUtils.toPolygon(map.visibleRegion),
            searchOptions,
            searchListener,
        )

    }

    companion object {
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


    fun boundZoom(p1: Point, p2: Point){
        var boundingBox = BoundingBox(p1, p2)
        var cameraPosition = map.cameraPosition(Geometry.fromBoundingBox(boundingBox)) // getting cameraPosition
        cameraPosition = CameraPosition(cameraPosition.target, cameraPosition.zoom - 0.8f, cameraPosition.azimuth, cameraPosition.tilt) // Zoom 80%
        map.move(cameraPosition, Animation(Animation.Type.SMOOTH, .5f), null) // move camera
    }

    private fun createBitmapFromVector(art: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, art) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        ) ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }



}