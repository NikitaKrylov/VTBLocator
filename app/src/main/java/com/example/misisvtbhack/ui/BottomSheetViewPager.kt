package com.example.misisvtbhack.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.LifecycleOwner
import com.example.misisvtbhack.MapViewModel
import com.example.misisvtbhack.components.MapKitService
import com.example.misisvtbhack.data.Office
import com.yandex.mapkit.geometry.Point

class BottomSheetViewPager(fm: FragmentManager, val mapService: MapKitService, val viewModel: MapViewModel, val owner: LifecycleOwner, val onRouteClick: (from: Point, to: Point) -> Unit, val onDetailClick: (office: Office) -> Unit) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment =
        when (position){
            0 -> OfficeListFragment(mapService, viewModel, owner, onRouteClick, onDetailClick)
            1 -> TermListFragment()
            else -> OfficeListFragment(mapService, viewModel, owner, onRouteClick, onDetailClick)
        }

    override fun getPageTitle(position: Int): CharSequence =
        when (position) {
            0 -> "Отделения"
            1 -> "Банкоматы"
            else -> "Отделения"
    }
}