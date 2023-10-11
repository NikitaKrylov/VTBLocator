package com.example.misisvtbhack.components

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.misisvtbhack.BankBranchAdapter
import com.example.misisvtbhack.data.BankBranch
import com.example.misisvtbhack.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetController(view: View) {
    private val bottomSheetBehavior = BottomSheetBehavior.from(view)
    private val binding = BottomSheetBinding.bind(view)
    private val _adapter = BankBranchAdapter()

    fun hide(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }
    fun collapse(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
    fun expand(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
    fun setBankBranches(banks: List<BankBranch>){
        binding.bankBranchRecycler.apply {
            visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = _adapter
        }
        _adapter.submitList(banks)
    }

}