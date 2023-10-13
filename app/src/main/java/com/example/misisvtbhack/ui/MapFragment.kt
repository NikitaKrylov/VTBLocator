package com.example.misisvtbhack.ui


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
import com.example.misisvtbhack.MapViewModel
import com.example.misisvtbhack.R
import com.example.misisvtbhack.components.BottomSheetController
import com.example.misisvtbhack.components.LocationService
import com.example.misisvtbhack.components.MapKitService
import com.example.misisvtbhack.data.Office
import com.google.gson.Gson


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

        val jsonText = requireActivity().assets.open("offices.json").bufferedReader().use { it.readText() }

//        val file = File("./data/offices.txt")
        val data = Gson().fromJson(jsonText, Array<Office>::class.java)

        viewModel.offices.postValue(data.toList())

        viewModel.offices.observe(viewLifecycleOwner){ offices ->
            val location = viewModel.currentLocation.value
            val point = Point(55.607445, 37.532282)

            if (location != null){
                val sortedBanks = offices.sortedByDescending { it.distanceTo(point) }.reversed()
                mapService.makePoints(sortedBanks)
                mBottomSheetController.setBankBranches(sortedBanks)
            }



        }
        viewModel.currentLocation.observe(viewLifecycleOwner){ loc ->
//            val point = Point(loc.latitude, loc.longitude)
            val point = Point(55.607445, 37.532282)
            val sortedbanks = viewModel.offices.value?.sortedByDescending { it.distanceTo(point) }?.reversed()

            sortedbanks?.let {
                mapService.makePoints(sortedbanks)
                mBottomSheetController.setBankBranches(sortedbanks)
            }
        }

//

        map.addInputListener(object : InputListener{
            override fun onMapTap(p0: Map, p1: Point) {

            }

            override fun onMapLongTap(p0: Map, p1: Point) {
                mapService.moveCamera(p1)
            }

        })
        

        //btn listener
//        binding.zoom1.setOnClickListener {
//            mapService.zoom(1f)
//
//        }
//        binding.zoom2.setOnClickListener {
//            mapService.zoom(-1f)
//
//        }
        binding.currentPosBtn.setOnClickListener {
            mapService.moveToCurrentPosition()
        }

        binding.mainFindButton.setOnClickListener {
            mBottomSheetController.expand()
        }

            locationService.setLocationUpdateListener()
            viewModel.currentLocation.observe(viewLifecycleOwner){location ->
                mapService.userPlaceMark.move(Point(location.latitude, location.longitude))

            }

    }

    fun goToOfficeDetail(data: Office){
//        findNavController().navigate(
//        )
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