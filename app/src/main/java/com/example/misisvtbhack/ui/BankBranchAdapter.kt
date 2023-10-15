package com.example.misisvtbhack.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.misisvtbhack.MapViewModel
import com.example.misisvtbhack.components.MapKitService
import com.example.misisvtbhack.data.Office
import com.example.misisvtbhack.databinding.BankBranchCardBinding
import com.yandex.mapkit.directions.driving.DrivingSummarySession
import com.yandex.mapkit.geometry.Point
import com.yandex.runtime.Error

class BankBranchAdapter(
    private val viewModel: MapViewModel,
    private val mapService: MapKitService,
    private val owner: LifecycleOwner,
    private val onRouteClick: (from: Point, to: Point) -> Unit,
    private val onDetailClick: (office: Office) -> Unit

    ) : ListAdapter<Office, BankBranchAdapter.BankBranchViewHolder>(diffCalculator) {

    inner class BankBranchViewHolder(private val binding: BankBranchCardBinding, onItemClick: (from: Point, to: Point) -> Unit) : RecyclerView.ViewHolder(binding.root){
        private val routeSummaryListener = object : DrivingSummarySession.DrivingSummaryListener {

            override fun onDrivingSummaries(summaries: MutableList<com.yandex.mapkit.directions.driving.Summary>) {
                if (summaries.isEmpty()) return TODO("Error")
                val data = summaries.first().weight

                binding.travelDistance.text = data.distance.text
                binding.travelTime.text = data.time.text
            }

            override fun onDrivingSummariesError(p0: Error) {
                TODO("Error")
            }

        }
        fun bind(data: Office){
            binding.apply {
                address.text = data.address
                name.text = data.salePointName

                moreInfo.setOnClickListener {
                    onDetailClick(data)
                }
                viewModel.currentLocation.value?.let{ loc ->
                    buildRouteBtn.setOnClickListener{
                        onRouteClick(Point(loc.latitude, loc.longitude), Point(data.latitude, data.longitude))
                    }
                }

            }





            binding.root.setOnClickListener {
                viewModel.currentLocation.value?.let{ loc ->
                    mapService.lazyBuildRoute(Point(loc.latitude, loc.longitude), Point(data.latitude, data.longitude), routeSummaryListener)
                }
                binding.detailBlock.isVisible = !binding.detailBlock.isVisible
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankBranchViewHolder {
        return BankBranchViewHolder(BankBranchCardBinding.inflate(LayoutInflater.from(parent.context)), onRouteClick )
    }

    override fun onBindViewHolder(holder: BankBranchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val diffCalculator = object : DiffUtil.ItemCallback<Office>(){
            override fun areItemsTheSame(oldItem: Office, newItem: Office): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Office, newItem: Office): Boolean = oldItem == newItem

        }
    }
}