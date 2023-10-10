package com.example.misisvtbhack


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.misisvtbhack.data.BankBranch
import com.example.misisvtbhack.databinding.FragmentMapBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import java.util.concurrent.TimeUnit


class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var mapService: MapKitService
    private lateinit var map: Map
    private lateinit var locationManager: LocationManager
    private val viewModel: MapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitFactory.setApiKey("90364c70-5b89-487f-89db-d3e305429c7c")
        MapKitFactory.initialize(context)
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map = binding.mapView.mapWindow.map
        mapService = MapKitService(requireContext(), map)
        mapService.moveCamera(Point(55.607445, 37.532282))

        val fakeBranches = listOf(
            BankBranch(Point(55.605670, 37.534763)),
            BankBranch(Point(55.656633, 37.621223)),
            BankBranch(Point(55.762919, 37.622122))
        )

        val pinsCollection = map.mapObjects.addCollection()
        fakeBranches.forEach{ branch ->
            pinsCollection.addPlacemark(branch.point)
        }


        map.addInputListener(object : InputListener{
            override fun onMapTap(p0: Map, p1: Point) {

            }

            override fun onMapLongTap(p0: Map, p1: Point) {
                mapService.moveCamera(p1)
            }

        })
        

        //zoom listener
        
        binding.zoom1.setOnClickListener {
            mapService.zoom(1f)

        }
        binding.zoom2.setOnClickListener {
            mapService.zoom(-1f)

        }

        //location listener
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
//        locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f
        ) { Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show() }


    }


}