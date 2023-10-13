package com.example.misisvtbhack

import android.app.Application
import com.example.misisvtbhack.data.Office
import com.google.gson.Gson
import com.yandex.mapkit.MapKitFactory

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        val jsonText = applicationContext.assets.open("offices.json").bufferedReader().use { it.readText() }

//        val file = File("./data/offices.txt")
        val data = Gson().fromJson(jsonText, Array<Office>::class.java)
    }
}