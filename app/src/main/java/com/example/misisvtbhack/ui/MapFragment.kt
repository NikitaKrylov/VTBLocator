package com.example.misisvtbhack.ui


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import androidx.fragment.app.viewModels
import com.example.misisvtbhack.MapViewModel
import com.example.misisvtbhack.R
import com.example.misisvtbhack.components.LocationService
import com.example.misisvtbhack.components.MapKitService
import com.example.misisvtbhack.components.bottomsheet.OfficeDetailBottomSheet
import com.example.misisvtbhack.components.bottomsheet.OfficeListBottomSheet
import com.example.misisvtbhack.data.DataBuilder
import com.example.misisvtbhack.data.Office
import com.example.misisvtbhack.databinding.FragmentMapBinding


class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mapService: MapKitService
    private lateinit var map: Map
    private lateinit var locationService: LocationService
    private lateinit var officeListBottomSheet: OfficeListBottomSheet
    private lateinit var officeDetailBottomSheet: OfficeDetailBottomSheet
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
        locationService = LocationService(requireActivity(), viewModel).apply {
            setLocationUpdateListener()
        }
        mapService = MapKitService(requireContext(), map, viewModel, this::showOfficeDetail)
        mapService.moveCamera(Point(55.607445, 37.532282))

        officeListBottomSheet = OfficeListBottomSheet(view.findViewById(R.id.bottomSheet), mapService, viewModel, viewLifecycleOwner, parentFragmentManager, this::buildRoute, this::showOfficeDetail)
        officeDetailBottomSheet = OfficeDetailBottomSheet(view.findViewById(R.id.bottomSheetDetail), viewModel, this::callTaxi, this::buildRoute).apply {
            hide()
        }


        viewModel.atms.observe(viewLifecycleOwner){
            mapService.makeAtmsPoints(it)
        }
        viewModel.offices.observe(viewLifecycleOwner){ offices ->
            mapService.makePoints(offices)

        }

        viewModel.currentLocation.observe(viewLifecycleOwner){ location ->
            //                mapService.userPlaceMark.move(Point(location.latitude, location.longitude))
            mapService.userPlaceMark.move(Point(55.607445, 37.532282))
        }

        val atmsData = DataBuilder.getTAtmsData(requireContext())
        viewModel.atms.postValue(atmsData.toList())

        val officesData = DataBuilder.getOfficeData(requireContext())
        officeListBottomSheet.setUp(officesData.toList())
        viewModel.offices.postValue(officesData.toList())


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
            showOfficeList()
        }

        viewModel.currentLocation.observe(viewLifecycleOwner){location ->
            Point(55.607445, 37.532282)
//                mapService.userPlaceMark.move(Point(location.latitude, location.longitude))
            mapService.userPlaceMark.move(Point(55.607445, 37.532282))

        }


        binding.search.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    mapService.search(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

    }

    fun showOfficeDetail(office: Office){
        officeListBottomSheet.collapse()
        officeDetailBottomSheet.setUpData(office)
        officeDetailBottomSheet.collapse()
    }

    fun showOfficeList(){
        officeListBottomSheet.expand()
        officeDetailBottomSheet.hide()

    }

    fun buildRoute(from: Point, to: Point){
        mapService.lazyBuildRoute(from, to)
        officeListBottomSheet.hide()
        officeDetailBottomSheet.hide()
    }

    fun callTaxi(from: Point, to: Point){
        val url = "https://3.redirect.appmetrica.yandex.com/route?" +
                "start-lat=${from.latitude}" +
                "&start-lon=${from.longitude}" +
                "&end-lat=${to.latitude}" +
                "&end-lon=${to.longitude}" +

                "&appmetrica_tracking_id=25395763362139037" +
                "&lang=ru"
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
        )
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