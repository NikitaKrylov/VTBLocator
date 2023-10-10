package com.example.misisvtbhack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.misisvtbhack.databinding.ActivityMainBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.maps.mobile.BuildConfig

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

//    override fun onStart() {
//        super.onStart()
//        MapKitFactory.getInstance().onStart()
//        binding.mapview.onStart()
//    }
//
//    override fun onStop() {
//        binding.mapview.onStop()
//        MapKitFactory.getInstance().onStop()
//        super.onStop()
//    }
}