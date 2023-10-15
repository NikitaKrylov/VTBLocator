package com.example.misisvtbhack.components.bottomsheet

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.example.misisvtbhack.MapViewModel
import com.example.misisvtbhack.components.MapKitService
import com.example.misisvtbhack.data.Office
import com.example.misisvtbhack.databinding.BottomSheetBinding
import com.example.misisvtbhack.ui.BankBranchAdapter
import com.example.misisvtbhack.ui.BottomSheetViewPager
import com.yandex.mapkit.geometry.Point

class OfficeListBottomSheet(view: View, mapService: MapKitService, viewModel: MapViewModel, owner: LifecycleOwner, fm: FragmentManager, onRouteClick: (from: Point, to: Point) -> Unit, onDetailClick: (office: Office) -> Unit) : BottomSheet(view) {
    private val binding = BottomSheetBinding.bind(view)
    private val _adapter = BankBranchAdapter(viewModel, mapService, owner, onRouteClick, onDetailClick)
    private val viewPagerAdapter = BottomSheetViewPager(fm, mapService, viewModel, owner, onRouteClick, onDetailClick)


    fun setUp(offices: List<Office>){
        binding.viewPager.adapter = viewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

}