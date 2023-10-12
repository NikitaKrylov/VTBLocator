package com.example.misisvtbhack


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.misisvtbhack.databinding.FragmentMapBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import androidx.fragment.app.viewModels
import com.example.misisvtbhack.components.BottomSheetController
import com.example.misisvtbhack.components.LocationService
import com.example.misisvtbhack.components.MapKitService
import com.example.misisvtbhack.data.BankBranch


class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var mapService: MapKitService
    private lateinit var map: Map
    private lateinit var locationService: LocationService
    private lateinit var mBottomSheetController: BottomSheetController
    private val viewModel: MapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitFactory.initialize(requireContext())
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map = binding.mapView.mapWindow.map
        locationService = LocationService(requireActivity(), viewModel)
        mapService = MapKitService(requireContext(), map, viewModel)
        mapService.moveCamera(Point(55.607445, 37.532282))

        mBottomSheetController = BottomSheetController(view.findViewById(R.id.bottomSheet), mapService, viewModel, viewLifecycleOwner)


        viewModel.bankBranches.observe(viewLifecycleOwner){ banks ->

            mapService.makePoints(banks)
            mBottomSheetController.setBankBranches(banks)

        }


        map.addInputListener(object : InputListener{
            override fun onMapTap(p0: Map, p1: Point) {

            }

            override fun onMapLongTap(p0: Map, p1: Point) {
                mapService.moveCamera(p1)
            }

        })
        

        //btn listener
        binding.zoom1.setOnClickListener {
            mapService.zoom(1f)

        }
        binding.zoom2.setOnClickListener {
            mapService.zoom(-1f)

        }
        binding.currentPosBtn.setOnClickListener {
            mapService.moveToCurrentPosition()
        }

        binding.mainFindButton.setOnClickListener {
            mBottomSheetController.expand()
        }


            locationService.setLocationUpdateListener()
            viewModel.currentLocation.observe(viewLifecycleOwner){location ->
                mapService.makePoint(Point(location.latitude, location.longitude))

            }
//        val updatedBanks = mutableListOf<Summary>()
//
//        val listener = CustomDrivingRouteListener(updatedBanks)
//
//
//        viewModel.bankBranches.addSource(viewModel.currentLocation) { location ->
//            viewModel.bankBranches.value?.forEach { bank ->
//                mapService.lazyBuildRoute(
////                    Point(location.latitude, location.longitude),
//                    Point(55.728312, 37.609500),
//                    bank.point,
//                    bank._drivingSummaryListener
//                )
//            }
//        }
//        viewModel.bankBranches.value

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