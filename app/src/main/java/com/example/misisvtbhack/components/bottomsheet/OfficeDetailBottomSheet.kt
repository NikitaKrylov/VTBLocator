package com.example.misisvtbhack.components.bottomsheet

import android.content.Intent
import android.view.View
import com.example.misisvtbhack.MapViewModel
import com.example.misisvtbhack.data.Office
import com.example.misisvtbhack.databinding.BottomSheetOfficeDetailBinding
import com.yandex.mapkit.geometry.Point
import kotlin.coroutines.coroutineContext

class OfficeDetailBottomSheet(view: View, val viewModel: MapViewModel, val callTaxi: (from: Point, to: Point) -> Unit, val buildRoute: (from: Point, to: Point) -> Unit) : BottomSheet(view) {

    private val binding = BottomSheetOfficeDetailBinding.bind(view)

    fun setUpData(data: Office){
        binding.apply {
            address.text = data.address
            name.text = data.salePointName
            metro.text = (data.metroStation ?: "Отсутствует").toString()
            schedule.text = data.openHours.joinToString(separator = "\n") {
                "${it.days} ${it.hours}"
            }
            viewModel.currentLocation.value?.let{loc ->
                binding.callTaxi.setOnClickListener{
                    callTaxi(Point(loc.latitude, loc.longitude), Point(data.latitude, data.longitude))
                }
                binding.materialButton.setOnClickListener{
                    buildRoute(Point(loc.latitude, loc.longitude), Point(data.latitude, data.longitude))
                }
            }

        }
    }
}