package com.example.misisvtbhack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.misisvtbhack.components.MapKitService
import com.example.misisvtbhack.data.BankBranch
import com.example.misisvtbhack.databinding.BankBranchCardBinding
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingSession.DrivingRouteListener
import com.yandex.mapkit.geometry.Point
import com.yandex.runtime.Error

class BankBranchAdapter(
    private val viewModel: MapViewModel,
    private val mapService: MapKitService,
    private val owner: LifecycleOwner,
    private val onItemClick: (from: Point, to: Point) -> Unit,

) : ListAdapter<BankBranch, BankBranchAdapter.BankBranchViewHolder>(diffCalculator) {

    inner class BankBranchViewHolder(private val binding: BankBranchCardBinding, onItemClick: (from: Point, to: Point) -> Unit) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: BankBranch){
            binding.title.text = data.travelTime

            binding.root.setOnClickListener {
//                    onItemClick(Point(location.latitude, location.longitude), data.point)
//                onItemClick(Point(55.728312, 37.609500), data.point)
                binding.newText.isVisible = !binding.newText.isVisible
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankBranchViewHolder {
        return BankBranchViewHolder(BankBranchCardBinding.inflate(LayoutInflater.from(parent.context)), onItemClick )
    }

    override fun onBindViewHolder(holder: BankBranchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val diffCalculator = object : DiffUtil.ItemCallback<BankBranch>(){
            override fun areItemsTheSame(oldItem: BankBranch, newItem: BankBranch): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: BankBranch, newItem: BankBranch): Boolean = oldItem == newItem

        }
    }
}