package com.example.misisvtbhack

import android.app.Application
import com.example.misisvtbhack.components.LocationService
import com.yandex.mapkit.MapKitFactory

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
    }
}