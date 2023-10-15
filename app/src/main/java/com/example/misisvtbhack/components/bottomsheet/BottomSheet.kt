package com.example.misisvtbhack.components.bottomsheet

import android.view.View
import com.example.misisvtbhack.databinding.BottomSheetBinding
import com.example.misisvtbhack.ui.BankBranchAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yandex.mapkit.geometry.Point

open class BottomSheet(view: View) {

    private val bottomSheetBehavior = BottomSheetBehavior.from(view)

    fun hide(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }
    fun collapse(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
    fun expand(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}