package com.example.misisvtbhack.components

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.misisvtbhack.ui.BankBranchAdapter
import com.example.misisvtbhack.MapViewModel
import com.example.misisvtbhack.data.Office
import com.example.misisvtbhack.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yandex.mapkit.geometry.Point

class BottomSheetController(view: View, mapService: MapKitService, viewModel: MapViewModel, owner: LifecycleOwner) {
    private val bottomSheetBehavior = BottomSheetBehavior.from(view)
    private val binding = BottomSheetBinding.bind(view)
    private val _adapter = BankBranchAdapter(viewModel, mapService, owner) { from: Point, to: Point ->
        collapse()
        mapService.lazyBuildRoute(from, to)
    }

    fun hide(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }
    fun collapse(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
    fun expand(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
    fun setBankBranches(offices: List<Office>){
        binding.bankBranchRecycler.apply {
            visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = _adapter
        }
        _adapter.submitList(offices)
    }

}