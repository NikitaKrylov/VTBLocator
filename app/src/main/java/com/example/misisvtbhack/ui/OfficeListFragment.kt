package com.example.misisvtbhack.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.misisvtbhack.BranchSorter
import com.example.misisvtbhack.MapViewModel
import com.example.misisvtbhack.components.MapKitService
import com.example.misisvtbhack.data.Office
import com.example.misisvtbhack.databinding.FragmentOfficeListBinding
import com.yandex.mapkit.geometry.Point


class OfficeListFragment(mapService: MapKitService, val viewModel: MapViewModel, val owner: LifecycleOwner, val onRouteClick: (from: Point, to: Point) -> Unit, val onDetailClick: (office: Office) -> Unit) : Fragment() {

    private lateinit var binding: FragmentOfficeListBinding
    private val _adapter = BankBranchAdapter(viewModel, mapService, owner, onRouteClick, onDetailClick)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOfficeListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bankBranchRecycler.apply {
            visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = _adapter
        }

        viewModel.offices.observe(owner) { offices ->
            viewModel.currentLocation.value?.let{ loc ->
                val sortedBranches = BranchSorter().sortOffices(Point(loc.latitude, loc.longitude), offices)
                _adapter.submitList(sortedBranches)
            }

        }
        viewModel.currentLocation.observe(owner) { loc ->
            val banks = viewModel.offices.value

            banks?.let{
                val sortedBranches = BranchSorter().sortOffices(Point(loc.latitude, loc.longitude), it)
                _adapter.submitList(sortedBranches)
            }
        }
    }


}