package com.example.misisvtbhack


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.misisvtbhack.data.BankBranch
import com.example.misisvtbhack.databinding.FragmentMapBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import androidx.fragment.app.viewModels
import com.example.misisvtbhack.components.BottomSheetController
import com.example.misisvtbhack.components.MapKitService
import java.util.function.Consumer


class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var mapService: MapKitService
    private lateinit var map: Map
    private lateinit var locationManager: LocationManager
    private lateinit var mBottomSheetController: BottomSheetController
    private val viewModel: MapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitFactory.initialize(requireContext())
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBottomSheetController = BottomSheetController(view.findViewById(R.id.bottomSheet))
        map = binding.mapView.mapWindow.map
        mapService = MapKitService(requireContext(), map)
        mapService.moveCamera(Point(55.607445, 37.532282))

        val fakeBranches = listOf(
            BankBranch(Point(55.605670, 37.534763)),
            BankBranch(Point(55.656633, 37.621223)),
            BankBranch(Point(55.656633, 37.611223)),
            BankBranch(Point(55.762919, 37.622122))
        )
        mapService.makePoints(fakeBranches)
        mBottomSheetController.setBankBranches(fakeBranches)
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
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }


        locationManager.getCurrentLocation(LocationManager.NETWORK_PROVIDER, null, requireActivity().application.mainExecutor
        ) {
            val point = Point(it.latitude, it.longitude)
            mapService.makePoint(point)
        }


        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 10f
        ) {
            val point = Point(it.latitude, it.longitude)
        }

//        val sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
//        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        mapService.buildRoute(Point(55.605670, 37.534763), Point(55.656633, 37.621223))

    }
    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

}